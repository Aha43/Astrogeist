package astrogeist.app.component.data.metadataview;

import java.awt.BorderLayout;
import java.util.LinkedHashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import astrogeist.timeline.TimelineValue;

public final class MetadataTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTable table;
	private final MetadataTableModel tableModel;

	public MetadataTablePanel() {
		super(new BorderLayout());
		
		this.tableModel = new MetadataTableModel();
		this.table = new JTable(this.tableModel);

		this.table.setFillsViewportHeight(true);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(this.table);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public void setData(LinkedHashMap<String, TimelineValue> data) { this.tableModel.setData(data); }
	public JTable getTable() { return this.table; }
	public MetadataTableModel getTableModel() { return this.tableModel; }
	
	public void clear() { this.tableModel.clear(); }

}
