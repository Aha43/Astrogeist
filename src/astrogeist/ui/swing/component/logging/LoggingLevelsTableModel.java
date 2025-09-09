package astrogeist.ui.swing.component.logging;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

public final class LoggingLevelsTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private static final List<Level> LEVELS = List.of(
        Level.OFF, Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG, Level.FINE, Level.FINER, Level.FINEST, Level.ALL
    );

    private final List<String> namespaces = new ArrayList<>();
    private final Map<String, Level> levels = new LinkedHashMap<>();

    public LoggingLevelsTableModel() {
        refreshFromLogManager();
    }

    public void refreshFromLogManager() {
        namespaces.clear();
        levels.clear();

        // Collect namespaces from current JUL registry
        var names = LogManager.getLogManager().getLoggerNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            // Show root logger as "*" (optional)
            if (name == null || name.isBlank()) name = "";
            namespaces.add(name);
        }

        // Sort: put astrogeist* first, then others
        namespaces.sort((a,b) -> {
            boolean aa = a.startsWith("astrogeist");
            boolean bb = b.startsWith("astrogeist");
            if (aa != bb) return aa ? -1 : 1;
            return a.compareTo(b);
        });

        // Snapshot displayed level (explicit level or effective)
        for (String ns : namespaces) {
            Logger l = Logger.getLogger(ns);
            Level explicit = l.getLevel();
            levels.put(ns, (explicit != null) ? explicit : effectiveLevel(l));
        }

        fireTableDataChanged();
    }

    // Compute effective (inherited) level for display hint
    private Level effectiveLevel(Logger l) {
        for (Logger cur = l; cur != null; cur = cur.getParent()) {
            if (cur.getLevel() != null) return cur.getLevel();
        }
        return Level.INFO; // JUL default if nothing set
    }

    public Map<String, Level> toPackageLevelMap() {
        Map<String, Level> map = new LinkedHashMap<>();
        for (String ns : namespaces) {
            map.put(ns, levels.get(ns));
        }
        return map;
    }

    @Override public int getRowCount() { return namespaces.size(); }
    @Override public int getColumnCount() { return 2; }

    @Override public String getColumnName(int c) {
        return (c == 0) ? "Namespace" : "Level";
    }

    @Override public Class<?> getColumnClass(int c) {
        return (c == 0) ? String.class : Level.class;
    }

    @Override public boolean isCellEditable(int row, int col) {
        return col == 1; // only the Level column is editable
    }

    @Override public Object getValueAt(int row, int col) {
        String ns = namespaces.get(row);
        return (col == 0) ? (ns.isEmpty() ? "*" : ns) : levels.get(ns);
    }

    @Override public void setValueAt(Object aValue, int row, int col) {
        if (col != 1) return;
        String ns = namespaces.get(row);
        if (aValue instanceof Level) {
            levels.put(ns, (Level) aValue);
            fireTableCellUpdated(row, col);
        } else if (aValue instanceof String) {
            try {
                levels.put(ns, Level.parse((String) aValue));
                fireTableCellUpdated(row, col);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public static List<Level> allLevels() { return LEVELS; }
}

