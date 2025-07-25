package astrogeist.app.component;

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
		tableModel = new PropertiesTableModel();
		table = new JTable(tableModel);

		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(table);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public void setProperties(Map<String, String> properties) { tableModel.setData(properties);; }
	public JTable getTable() { return table; }
	public PropertiesTableModel getTableModel() { return tableModel; }

}
