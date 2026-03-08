package aha.common.ui.swing.diagnostic.idnames;

import static java.util.Objects.requireNonNull; 

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aha.common.abstraction.IdNames;

public final class IdNamesTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private final List<Map.Entry<String,String>> rows;

    public IdNamesTableModel(IdNames idNames) {
    	requireNonNull(idNames, "idNames");
        this.rows = new ArrayList<>(idNames.entries().entrySet());
        this.rows.sort(Map.Entry.comparingByKey());
    }

    @Override public final int getRowCount() { return this.rows.size(); }
    @Override public final int getColumnCount() { return 2; }

    @Override public final String getColumnName(int col) {
        return switch (col) {
            case 0 -> "Id";
            case 1 -> "Label";
            default -> throw new IllegalArgumentException();
        };
    }

    @Override public final Object getValueAt(int row, int col) {
        var entry = this.rows.get(row);

        return switch (col) {
            case 0 -> entry.getKey();
            case 1 -> entry.getValue();
            default -> throw new IllegalArgumentException();
        };
    }
    
}