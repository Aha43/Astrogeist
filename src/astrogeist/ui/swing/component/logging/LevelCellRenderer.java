package astrogeist.ui.swing.component.logging;

import java.awt.Component;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public final class LevelCellRenderer implements TableCellRenderer {
    private final JComboBox<Level> combo = new JComboBox<>();

    public LevelCellRenderer() {
        for (Level lv : LoggingLevelsTableModel.allLevels()) combo.addItem(lv);
        combo.setBorder(BorderFactory.createEmptyBorder());
        combo.setEnabled(false); // render-only look
    }

    @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        combo.setSelectedItem(value instanceof Level ? value : Level.INFO);
        if (isSelected) combo.setBackground(table.getSelectionBackground());
        else combo.setBackground(table.getBackground());
        return combo;
    }
}
