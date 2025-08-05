package astrogeist.app.component.data.metadataview;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import astrogeist.timeline.TimelineUtil;
import astrogeist.timeline.TimelineValue;
import astrogeist.typesystem.Type;

public class MetadataTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private final List<Map.Entry<String, TimelineValue>> entries = new ArrayList<>();

    public void setData(LinkedHashMap<String, TimelineValue> data) {
        this.entries.clear();
        if (data != null) {
        	var withNoFiles = TimelineUtil.getExcludingTypeMap(data, Type.DiskFile());
            this.entries.addAll(withNoFiles.entrySet());
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() { return this.entries.size(); }
    @Override
    public int getColumnCount() { return 3; } // Property, Value, Type

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Map.Entry<String, TimelineValue> entry = this.entries.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> entry.getKey();
            case 1 -> entry.getValue().value();
            case 2 -> entry.getValue().type();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Property";
            case 1 -> "Value";
            case 2 -> "Type";
            default -> "";
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) { return false; } // all columns read-only

    public void clear() { this.entries.clear(); fireTableDataChanged(); }
}
