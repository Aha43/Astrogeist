package astrogeist.app.dialog.settings;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public final class SettingsTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    private final List<String> keys;
    private final List<String> values;

    public SettingsTableModel(Map<String, String> data) {
        keys = new ArrayList<>(data.keySet());
        values = new ArrayList<>();
        for (String k : keys) values.add(data.get(k));
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }
        return map;
    }

    @Override
    public int getRowCount() { return keys.size(); }
    @Override
    public int getColumnCount() { return 2; }
    @Override
    public String getColumnName(int column) { return column == 0 ? "Setting" : "Value"; }
    @Override
    public Object getValueAt(int row, int column) {
        return column == 0 ? keys.get(row) : values.get(row); }
    @Override
    public boolean isCellEditable(int row, int column) { return false; } // all done with dialogs.
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (column == 1) values.set(row, String.valueOf(aValue)); }
}

