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

import astrogeist.engine.abstraction.persistence.AstrogeistStorageManager;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.engine.persitence.settings.Settings;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.selectionaction.AbstractSelectionAction;
import astrogeist.ui.swing.component.data.timeline.selectionaction.NoSelectionAction;
import astrogeist.ui.swing.component.data.timeline.selectionaction.SelectionActionsComboBox;
import astrogeist.ui.swing.dialog.message.MessageDialogs;
import astrogeist.ui.swing.dialog.selection.SelectionDialog;

/**
 * <p>
 *   Panel that shows TimelineView. Used both to show complete time line (Timeline) and filtered
 *   time line (TimelineView). Got some common tools but implementations may / can add buttons 
 *   north / south.
 * </p>
 */
public abstract class AbstractTimelineViewTablePanel  extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private TimelineNames timelineNames;
	
	protected final JTable table;
	protected final AbstractTimelineViewTableModel model;
	
	protected final JPanel northPanel;
	protected final JPanel southPanel;
	
	protected final SnapshotSelectionService snapshotSelectionService;
	
	protected final App app;
	
	protected final AstrogeistStorageManager astrogeistStorageManager;
	protected final Settings settings;

	protected AbstractTimelineViewTablePanel(
		App app,
		AstrogeistStorageManager astrogeistStorageManager,
		AbstractTimelineViewTableModel model,
		TimelineNames timelineNames,
		SnapshotSelectionService snapshotSelectionService) {
		
		super(new BorderLayout());
		
		this.astrogeistStorageManager = astrogeistStorageManager;
		
		this.snapshotSelectionService = snapshotSelectionService;
		
		this.app = app;
		
		this.timelineNames = timelineNames;
		
		this.northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		super.add(this.northPanel, BorderLayout.NORTH);
		this.southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		super.add(this.southPanel, BorderLayout.SOUTH);
		
		this.model = model;
		this.table = new JTable(this.model);

		this.table.setFillsViewportHeight(true);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(this.table);
		this.add(scrollPane, BorderLayout.CENTER);
		
		this.settings = loadSettings();
		
		this.model.setColumnsToShow(settings.getCsv(Settings.TABLE_COLUMNS));
		
		populateNorthPanel();
		populateSouthPanel();
		
		this.addaptEventSource();
	}
	
	private final Settings loadSettings() {
    	try {
    		var retVal = this.astrogeistStorageManager.load(Settings.class);
    		return retVal;
    	} catch (Exception x) {
    		MessageDialogs.showError(this, "Failed to load config", x);
    		return new astrogeist.engine.persitence.settings.Settings();
    	}
    }
	
	private final void addaptEventSource() {
		this.table.getSelectionModel().addListSelectionListener(e -> { 
			var index = this.table.getSelectedRow();
			if (index < 0) return;
			var timestamp = this.getTimestampAt(index);
			var snapshot = this.model.getSnapshotAt(index);
			this.snapshotSelectionService.selected(timestamp, snapshot);
		});
	}
	
	private final void populateNorthPanel() {
		var selectionActionsComboBox = new SelectionActionsComboBox();
		
		this.northPanel.add(new JLabel("Selection Action: "));
		this.northPanel.add(selectionActionsComboBox);
		
		this.table.getSelectionModel().addListSelectionListener(e -> {
			if (e.getValueIsAdjusting()) return;
	        int viewRow = table.getSelectedRow();
	        if (viewRow < 0) return;

	        var action =  (AbstractSelectionAction)selectionActionsComboBox.getSelectedItem();
	        if (action == null || action == NoSelectionAction.INSTANCE) return;

	        int modelRow = table.convertRowIndexToModel(viewRow);
	        var snapshot = this.model.getSnapshotAt(modelRow);
	        if (snapshot == null) return;

	        action.Perform(snapshot);
		});
	}
	
	private final void populateSouthPanel() {
		var columnsButton = new JButton("Select columns to show");
		
		columnsButton.addActionListener(e -> {
			var all = this.timelineNames.getTimelineNames();
			var selected = this.settings.getCsv(Settings.TABLE_COLUMNS);
			SelectionDialog.show(this.app, "Select Columns", selected, all);
			this.model.setColumnsToShow(selected);
			saveSelectedColumns(selected);
		});
		
		southPanel.add(columnsButton);
	}
	
	private final void saveSelectedColumns(List<String> selected) {
		try {
			var setting = String.join(", ", selected);
			this.settings.set(Settings.TABLE_COLUMNS, setting);
			this.astrogeistStorageManager.save(this.settings);
		} catch (Exception x) {
			MessageDialogs.showError(null, "Failed saving selection", x);
		}	
	}
	
	protected final void postSetData() {
		this.snapshotSelectionService.cleared();
		this.model.setColumnsToShow(this.settings.getCsv(Settings.TABLE_COLUMNS));
		this.table.getColumnModel().getColumn(0).setPreferredWidth(150);
	}
	
	public final void settingsUpdated() { 
		this.model.setColumnsToShow(this.settings.getCsv(Settings.TABLE_COLUMNS)); }
	
	public final int getSelectedRow() {
		int selectedRow = this.table.getSelectedRow();
		if (selectedRow == -1) return -1;
		int modelRow = this.table.convertRowIndexToModel(selectedRow);
		return modelRow;
	}
	
	public final Map<String, TimelineValue> getSelectedSnapshot() {
		var selectedRow = this.getSelectedRow();
		if (selectedRow == -1) return null;
		return this.model.getSnapshotAt(selectedRow);
	}
	
	public final Instant getSelectedTimestamp() { 
		var selectedRow = this.getSelectedRow();
		if (selectedRow == -1) return null;
		return getTimestampAt(selectedRow);
	}
	
	public final Instant getTimestampAt(int row) { return this.model.getTimestampAt(row); }
}
