package aha.common.ui.swing.panels.namevalueunit;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NameValueUnitTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private final List<NameValueUnitEntry> rows = new ArrayList<>();

    private static final String[] COLUMN_NAMES = { "Name", "Value", "Unit" };

    @Override public final int getRowCount() { return rows.size(); }

    @Override public final int getColumnCount() { return COLUMN_NAMES.length; }

    @Override public final String getColumnName(int col) {
    	return COLUMN_NAMES[col]; }

    @Override public final Object getValueAt(int rowIndex, int columnIndex) {
        var entry = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> entry.getName();
            case 1 -> entry.getValue().value();
            case 2 -> entry.getUnit();
            default -> null;
        };
    }

    // non-editable cells – editing via dialog only
    @Override public final boolean isCellEditable(int row, int col) { 
    	return false; }

    // Convenience API

    public final void addEntry(NameValueUnitEntry entry) {
        int idx = rows.size();
        rows.add(entry);
        fireTableRowsInserted(idx, idx);
    }

    public final void updateEntry(int rowIndex, NameValueUnitEntry newEntry) {
        rows.set(rowIndex, newEntry);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public final void removeEntry(int rowIndex) {
        rows.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public final NameValueUnitEntry entryAt(int rowIndex) {
    	return rows.get(rowIndex); }

    public final List<NameValueUnitEntry> entries() {
        return Collections.unmodifiableList(rows); }

    public final void entries(List<NameValueUnitEntry> entries) {
        rows.clear();
        rows.addAll(entries);
        fireTableDataChanged();
    }
    
}
