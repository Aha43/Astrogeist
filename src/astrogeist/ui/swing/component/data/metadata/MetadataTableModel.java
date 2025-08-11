package astrogeist.ui.swing.component.data.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import astrogeist.engine.timeline.TimelineSnapshotUtil;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.typesystem.Type;

public final class MetadataTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private final List<Map.Entry<String, TimelineValue>> entries = new ArrayList<>();

    public void setData(Map<String, TimelineValue> data) {
        this.entries.clear();
        if (data != null) {
        	var withNoFiles = TimelineSnapshotUtil.getExcludingTypeMap(data, Type.DiskFile());
            this.entries.addAll(withNoFiles.entrySet());
        }
        fireTableDataChanged();
    }

    @Override public final int getRowCount() { return this.entries.size(); }
    @Override public final int getColumnCount() { return 3; } // Property, Value, Type
    @Override public final boolean isCellEditable(int row, int col) { return false; }
    
    @Override public final Object getValueAt(int rowIndex, int columnIndex) {
        Map.Entry<String, TimelineValue> entry = this.entries.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> entry.getKey();
            case 1 -> entry.getValue().value();
            case 2 -> entry.getValue().type();
            default -> null;
        };
    }

    @Override public final String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Property";
            case 1 -> "Value";
            case 2 -> "Type";
            default -> "";
        };
    }

    public final void clear() { this.entries.clear(); fireTableDataChanged(); }
}
