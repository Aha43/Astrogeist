package astrogeist.app.component.propertiesview;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public final class PropertiesTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTable _table;
	private final PropertiesTableModel _tableModel;

	public PropertiesTablePanel() {
		super(new BorderLayout());
		_tableModel = new PropertiesTableModel();
		_table = new JTable(_tableModel);

		_table.setFillsViewportHeight(true);
		_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(_table);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public void setProperties(Map<String, String> properties) { _tableModel.setData(properties); }
	public JTable getTable() { return _table; }
	public PropertiesTableModel getTableModel() { return _tableModel; }
	
	public void clear() { _tableModel.clear(); }

}
