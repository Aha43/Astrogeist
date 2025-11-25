package astrogeist.ui.swing.component.data.files;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public final class FilePropertiesTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private final List<String> keys = new ArrayList<>();
	private final List<String> values = new ArrayList<>();

	@Override public final int getRowCount() { return keys.size(); }
	@Override public final int getColumnCount() { return 2; }
	
	@Override public final String getColumnName(int col) {
		return switch (col) {
			case 0 -> "Property";
			case 1 -> "Value";
			default -> "";
		};
	}
	
	@Override public final Object getValueAt(int row, int col) {
		return col == 0 ? keys.get(row) : values.get(row); }

	public final void setProperties(String[][] props) {
		keys.clear();
		values.clear();
		for (var p : props) {
			keys.add(p[0]);
			values.add(p[1]);
		}
		fireTableDataChanged();
	}
	
}
