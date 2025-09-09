package astrogeist.ui.swing.component.logging;

import java.awt.Component;
import java.util.logging.Level;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public final class LevelCellEditor extends AbstractCellEditor implements TableCellEditor {
    private static final long serialVersionUID = 1L;

    private final JComboBox<Level> combo = new JComboBox<>();

    public LevelCellEditor() {
        for (Level lv : LoggingLevelsTableModel.allLevels()) combo.addItem(lv);
    }

    @Override public Object getCellEditorValue() { return combo.getSelectedItem(); }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        combo.setSelectedItem(value instanceof Level ? value : Level.INFO);
        return combo;
    }
}

