package astrogeist.ui.swing.component.data.timeline.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.scanner.NormalizedProperties;
import astrogeist.engine.setting.SettingKeys;
import astrogeist.engine.setting.Settings;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.selectionaction.AbstractSelectionAction;
import astrogeist.ui.swing.component.data.timeline.selectionaction.NoSelectionAction;
import astrogeist.ui.swing.component.data.timeline.selectionaction.SelectionActionsComboBox;
import astrogeist.ui.swing.dialog.message.MessageDialogs;
import astrogeist.ui.swing.dialog.selection.SelectionDialog;

public abstract class AbstractTimelineViewTablePanel  extends JPanel {
	private static final long serialVersionUID = 1L;
	
	protected final JTable table;
	protected final AbstractTimelineViewTableModel tableModel;
	
	protected JPanel buttonsPanel;
	
	protected final App app;

	protected AbstractTimelineViewTablePanel(App app, AbstractTimelineViewTableModel model) {
		super(new BorderLayout());
		
		this.app = app;
		
		this.tableModel = model;
		this.table = new JTable(this.tableModel);

		this.table.setFillsViewportHeight(true);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(this.table);
		this.add(scrollPane, BorderLayout.CENTER);
		
		this.tableModel.setColumnsToShow(Settings.getCsv(SettingKeys.TABLE_COLUMNS));
		
		createButtonPanel();
	}
	
	public final void settingsUpdated() { 
		this.tableModel.setColumnsToShow(Settings.getCsv(SettingKeys.TABLE_COLUMNS)); }
	
	private final void createButtonPanel() {
		this.buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		var columnsButton = new JButton("Select columns to show");
		
		columnsButton.addActionListener(e -> {
			var all = NormalizedProperties.getNormalizedNamesAndUserDataNames();
			var selected = Settings.getCsv(SettingKeys.TABLE_COLUMNS);
			SelectionDialog.show(this.app, "Select Columns", selected, all);
			this.tableModel.setColumnsToShow(selected);
			saveSelectedColumns(selected);
		});
		
		buttonsPanel.add(columnsButton);
		
		this.add(buttonsPanel, BorderLayout.SOUTH);
		this.add(createNorthPanel(), BorderLayout.NORTH);
	}
	
	private final JPanel createNorthPanel() {
		var retVal = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		var selectionActionsComboBox = new SelectionActionsComboBox();
		
		retVal.add(new JLabel("Selection Action: "));
		retVal.add(selectionActionsComboBox);
		
		this.table.getSelectionModel().addListSelectionListener(e -> {
			if (e.getValueIsAdjusting()) return;
	        int viewRow = table.getSelectedRow();
	        if (viewRow < 0) return;

	        var action =  (AbstractSelectionAction)selectionActionsComboBox.getSelectedItem();
	        if (action == null || action == NoSelectionAction.INSTANCE) return;

	        int modelRow = table.convertRowIndexToModel(viewRow);
	        var snapshot = this.tableModel.getSnapshotAt(modelRow); // implement this getter
	        if (snapshot == null) return;

	        action.Perform(snapshot);
		});
		
		return retVal;
	}
	
	private final static void saveSelectedColumns(List<String> selected) {
		try {
			var setting = String.join(", ", selected);
			Settings.set(SettingKeys.TABLE_COLUMNS, setting);
			Settings.save();
		} catch (Exception x) {
			MessageDialogs.showError(null, "Failed saving selection", x);
		}	
	}
	
	protected final void postSetData() {
		this.app.getMetadataTablePanel().clear();
		this.app.getFilesPanel().clear();
		this.tableModel.setColumnsToShow(Settings.getCsv(SettingKeys.TABLE_COLUMNS));
		this.table.getColumnModel().getColumn(0).setPreferredWidth(150);
	}
	
	public final TimelineView getTimelineView() { return this.tableModel.getTimelineView(); }
	
	public final JTable getTable() { return this.table; }
	
	public final void addSelectionListener(ListSelectionListener l) {
		this.table.getSelectionModel().addListSelectionListener(l); }
	
	public final int getSelectedRow() {
		int selectedRow = this.table.getSelectedRow();
		if (selectedRow == -1) return -1;
		int modelRow = this.table.convertRowIndexToModel(selectedRow);
		return modelRow;
	}
	
	public final Map<String, TimelineValue> getSelectedSnapshot() {
		var selectedRow = this.getSelectedRow();
		if (selectedRow == -1) return null;
		return this.tableModel.getSnapshotAt(selectedRow);
	}
	
	public final Instant getTimestampAtRow(int rowIndex) { 
		return this.tableModel.getTimestampAt(rowIndex); }
}
