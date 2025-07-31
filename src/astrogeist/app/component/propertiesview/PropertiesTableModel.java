package astrogeist.app.component.propertiesview;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class PropertiesTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
	private final List<Map.Entry<String, String>> entries = new ArrayList<>();

    public void setData(LinkedHashMap<String, String> map) {
    	this.entries.clear();
        if (map != null) { this.entries.addAll(map.entrySet()); }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() { return this.entries.size(); }

    @Override
    public int getColumnCount() { return 2; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Map.Entry<String, String> entry = this.entries.get(rowIndex);
        return columnIndex == 0 ? entry.getKey() : entry.getValue();
    }

    @Override
    public String getColumnName(int column) { return column == 0 ? "Property" : "Value"; }

    @Override
    public boolean isCellEditable(int row, int column) { return false; }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
        	this.entries.get(rowIndex).setValue(String.valueOf(aValue));
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
    
    public void clear() { this.entries.clear(); fireTableDataChanged(); }
}
