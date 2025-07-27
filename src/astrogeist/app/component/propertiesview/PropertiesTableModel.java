package astrogeist.app.component.propertiesview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class PropertiesTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
	private final List<Map.Entry<String, String>> _entries = new ArrayList<>();

    public void setData(Map<String, String> map) {
        _entries.clear();
        if (map != null) {
            _entries.addAll(map.entrySet());
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() { return _entries.size(); }

    @Override
    public int getColumnCount() { return 2; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Map.Entry<String, String> entry = _entries.get(rowIndex);
        return columnIndex == 0 ? entry.getKey() : entry.getValue();
    }

    @Override
    public String getColumnName(int column) { return column == 0 ? "Property" : "Value"; }

    @Override
    public boolean isCellEditable(int row, int column) { return false; }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            _entries.get(rowIndex).setValue(String.valueOf(aValue));
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
    
    public void clear() { _entries.clear(); fireTableDataChanged(); }
}
