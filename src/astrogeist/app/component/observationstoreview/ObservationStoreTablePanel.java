package astrogeist.app.component.observationstoreview;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import astrogeist.app.dialog.userprops.UserPropsDialog;
import astrogeist.scanner.NormalizedProperties;
import astrogeist.setting.SettingKeys;
import astrogeist.setting.Settings;
import astrogeist.store.ObservationStore;

public final class ObservationStoreTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTable _table;
	private final ObservationStoreTableModel _tableModel;
	
	private final PropertiesTablePanel _propertiesTablePanel;
	private final ObservationFilesPanel _observationFilesPanel;
	
	private final App _app;

	public ObservationStoreTablePanel(
		App app,
		PropertiesTablePanel propertiesTablePanel,
		ObservationFilesPanel observationFilesPanel) {
		
		super(new BorderLayout());
		
		_app = app;
		
		_propertiesTablePanel = propertiesTablePanel;
		_observationFilesPanel = observationFilesPanel;
		
		_tableModel = new ObservationStoreTableModel();
		_table = new JTable(_tableModel);

		_table.setFillsViewportHeight(true);
		_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(_table);
		this.add(scrollPane, BorderLayout.CENTER);
		
		_tableModel.setColumnsToShow(Settings.getCsv(SettingKeys.TABLE_COLUMNS));
		
		createButtonPanel();
	}
	
	public void settingsUpdated() { _tableModel.setColumnsToShow(Settings.getCsv(SettingKeys.TABLE_COLUMNS)); }
	
	private void createButtonPanel() {
		var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		var columnsButton = new JButton("Select properties to show");
		
		columnsButton.addActionListener(e -> {
			var all = NormalizedProperties.getNormalizedNames();
			var selected = Settings.getCsv(SettingKeys.TABLE_COLUMNS);
			SelectionDialog.showDialog(_app, "Select Columns", selected, all);
			_tableModel.setColumnsToShow(selected);
			saveSelectedColumns(selected);
		});
		
		buttonPanel.add(columnsButton);
		
		var userPropsButton = new JButton("User properties");
		userPropsButton.addActionListener(e -> {
			UserPropsDialog.ShowDialog(_app);
		});
		buttonPanel.add(userPropsButton);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private static void saveSelectedColumns(List<String> selected) {
		try {
			var setting = String.join(", ", selected);
			Settings.set(SettingKeys.TABLE_COLUMNS, setting);
			Settings.save();
		} catch (Exception x) {
			MessageDialogs.showError(null, "Failed saving selection");
		}	
	}

	public void setStore(ObservationStore store) { 
		_tableModel.setStore(store); 
		_propertiesTablePanel.clear();
		_observationFilesPanel.clear();
		_tableModel.setColumnsToShow(Settings.getCsv(SettingKeys.TABLE_COLUMNS));
	}
	
	public ObservationStore getStore() { return _tableModel.getStore(); }
	public JTable getTable() { return _table; }
	public ObservationStoreTableModel getTableModel() { return _tableModel; }
	
	public void addSelectionListener(ListSelectionListener l) {
		_table.getSelectionModel().addListSelectionListener(l);
	}

}
