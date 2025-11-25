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

/**
 * <p>
 *   Panel showing selected snapshot's meta data.
 * </p>
 */
public final class MetadataTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTable table;
	private final MetadataTableModel tableModel;

	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param sss Service that {@code this} get snapshot selection events from.
	 */
	public MetadataTablePanel(SnapshotSelectionService sss) {
		super(new BorderLayout());
		
		this.tableModel = new MetadataTableModel();
		this.table = new JTable(this.tableModel);

		this.table.setFillsViewportHeight(true);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(this.table);
		this.add(scrollPane, BorderLayout.CENTER);
		
		sss.addListener(new SnapshotListener() {
			@Override public void onSnapshotSelected(Instant timestamp, 
				Map<String, TimelineValue> snapshot) {
					tableModel.setData(snapshot); }
			@Override public void onSelectionCleared() { tableModel.clear(); }
		});
	}
}
