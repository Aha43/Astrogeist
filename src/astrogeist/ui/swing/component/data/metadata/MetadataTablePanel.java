package astrogeist.ui.swing.component.data.metadata;

import java.awt.BorderLayout;
import java.time.Instant;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import astrogeist.engine.abstraction.selection.SnapshotListener;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.timeline.TimelineValue;

public final class MetadataTablePanel extends JPanel implements SnapshotListener {
	private static final long serialVersionUID = 1L;
	
	private final JTable table;
	private final MetadataTableModel tableModel;

	public MetadataTablePanel(SnapshotSelectionService snapshotSelectionService) {
		super(new BorderLayout());
		
		this.tableModel = new MetadataTableModel();
		this.table = new JTable(this.tableModel);

		this.table.setFillsViewportHeight(true);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(this.table);
		this.add(scrollPane, BorderLayout.CENTER);
		
		snapshotSelectionService.addListener(this);
	}

	public final void setSnapshot(Map<String, TimelineValue> data) { this.tableModel.setData(data); }
	public final JTable getTable() { return this.table; }
	public final MetadataTableModel getTableModel() { return this.tableModel; }
	
	public final void clear() { this.tableModel.clear(); }

	@Override public void onSnapshotSelected(Instant timestamp, Map<String, TimelineValue> snapshot) {
		this.setSnapshot(snapshot); }

	@Override public final void onSelectionCleared() { this.clear(); }
}
