package astrogeist.app.component.observationstoreview;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;

import astrogeist.app.App;
import astrogeist.app.component.fileview.ObservationFilesPanel;
import astrogeist.app.component.propertiesview.PropertiesTablePanel;
import astrogeist.app.dialog.message.MessageDialogs;
import astrogeist.app.dialog.selection.SelectionDialog;
import astrogeist.app.dialog.userdata.UserDataDialog;
import astrogeist.scanner.NormalizedProperties;
import astrogeist.setting.SettingKeys;
import astrogeist.setting.Settings;
import astrogeist.store.ObservationStore;
import astrogeist.userdata.UserDataIo;

public final class ObservationStoreTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTable table;
	private final ObservationStoreTableModel tableModel;
	
	private final PropertiesTablePanel propertiesTablePanel;
	private final ObservationFilesPanel observationFilesPanel;
	
	private final App app;

	public ObservationStoreTablePanel(
		App app,
		PropertiesTablePanel propertiesTablePanel,
		ObservationFilesPanel observationFilesPanel) {
		
		super(new BorderLayout());
		
		this.app = app;
		
		this.propertiesTablePanel = propertiesTablePanel;
		this.observationFilesPanel = observationFilesPanel;
		
		this.tableModel = new ObservationStoreTableModel();
		this.table = new JTable(this.tableModel);

		this.table.setFillsViewportHeight(true);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(this.table);
		this.add(scrollPane, BorderLayout.CENTER);
		
		this.tableModel.setColumnsToShow(Settings.getCsv(SettingKeys.TABLE_COLUMNS));
		
		createButtonPanel();
	}
	
	public void settingsUpdated() { this.tableModel.setColumnsToShow(Settings.getCsv(SettingKeys.TABLE_COLUMNS)); }
	
	public void update(Instant t, LinkedHashMap<String, String> values) { this.tableModel.update(t, values); }
	
	private void createButtonPanel() {
		var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		var columnsButton = new JButton("Select columns to show");
		
		columnsButton.addActionListener(e -> {
			var all = NormalizedProperties.getNormalizedNamesAndUserDataNames();
			var selected = Settings.getCsv(SettingKeys.TABLE_COLUMNS);
			SelectionDialog.showDialog(this.app, "Select Columns", selected, all);
			this.tableModel.setColumnsToShow(selected);
			saveSelectedColumns(selected);
		});
		
		buttonPanel.add(columnsButton);
		
		var userPropsButton = new JButton("Edit User Data");
		userPropsButton.addActionListener(e -> editUserData());
		buttonPanel.add(userPropsButton);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private static void saveSelectedColumns(List<String> selected) {
		try {
			var setting = String.join(", ", selected);
			Settings.set(SettingKeys.TABLE_COLUMNS, setting);
			Settings.save();
		} catch (Exception x) {
			MessageDialogs.showError(null, "Failed saving selection", x);
		}	
	}
	
	private void editUserData() {
		try {
			var selectedRow = this.getSelectedRow();
			if (selectedRow == -1) return;
		
			var t = this.getTimestampAtRow(selectedRow);
			var userData = UserDataIo.load(t);
			
			UserDataDialog.ShowDialog(this.app, t, userData);
		} catch(Exception x) {
			MessageDialogs.showError(this, "Failed to load user data", x);
		}
	}

	public void setStore(ObservationStore store) { 
		this.tableModel.setStore(store); 
		this.propertiesTablePanel.clear();
		this.observationFilesPanel.clear();
		this.tableModel.setColumnsToShow(Settings.getCsv(SettingKeys.TABLE_COLUMNS));
	}
	
	public ObservationStore getStore() { return this.tableModel.getStore(); }
	public JTable getTable() { return this.table; }
	public ObservationStoreTableModel getTableModel() { return this.tableModel; }
	
	public void addSelectionListener(ListSelectionListener l) {
		this.table.getSelectionModel().addListSelectionListener(l);
	}
	
	public int getSelectedRow() {
		int selectedRow = this.table.getSelectedRow();
		if (selectedRow == -1) return -1;
		int modelRow = this.table.convertRowIndexToModel(selectedRow);
		return modelRow;
	}
	
	public Instant getTimestampAtRow(int rowIndex) { return this.tableModel.getTimestampAt(rowIndex); }
	
}
