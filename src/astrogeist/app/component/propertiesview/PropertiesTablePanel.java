package astrogeist.app.component.propertiesview;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public final class PropertiesTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTable table;
	private final PropertiesTableModel tableModel;

	public PropertiesTablePanel() {
		super(new BorderLayout());
		
		this.tableModel = new PropertiesTableModel();
		this.table = new JTable(this.tableModel);

		this.table.setFillsViewportHeight(true);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(this.table);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public void setProperties(Map<String, String> properties) { this.tableModel.setData(properties); }
	public JTable getTable() { return this.table; }
	public PropertiesTableModel getTableModel() { return this.tableModel; }
	
	public void clear() { this.tableModel.clear(); }

}
