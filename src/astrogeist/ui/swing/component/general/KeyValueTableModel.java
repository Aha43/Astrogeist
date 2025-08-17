package astrogeist.ui.swing.component.general;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

/** Editable String→String pairs with duplicate-key protection. */
public final class KeyValueTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private static final String[] COLS = {"Key", "Value"};

    private static final class Pair {
        String key = "";
        String value = "";
        Pair() {}
        Pair(String k, String v) { this.key = k == null ? "" : k; this.value = v == null ? "" : v; }
    }

    private final java.util.List<Pair> rows = new ArrayList<>();
    private String lastErrorMessage = null;

    /** Start empty. */
    public KeyValueTableModel() {}

    /** Start with initial values (order preserved by iteration order of the map). */
    public KeyValueTableModel(Map<String, String> initial) {
        if (initial != null) {
            for (Map.Entry<String, String> e : initial.entrySet()) {
                rows.add(new Pair(e.getKey(), e.getValue()));
            }
        }
    }

    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return COLS.length; }
    @Override public String getColumnName(int column) { return COLS[column]; }
    @Override public Class<?> getColumnClass(int columnIndex) { return String.class; }
    @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return true; }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        Pair p = rows.get(rowIndex);
        return (columnIndex == 0) ? p.key : p.value;
    }

    @Override public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        String text = aValue == null ? "" : aValue.toString();
        Pair p = rows.get(rowIndex);

        if (columnIndex == 0) { // editing key
            String newKey = text.trim();
            if (isDuplicateKey(newKey, rowIndex)) {
                lastErrorMessage = "Duplicate key: \"" + newKey + "\"";
                Toolkit.getDefaultToolkit().beep();
                // keep old value; do not fire update
                return;
            }
            p.key = newKey;
        } else { // editing value
            p.value = text;
        }
        lastErrorMessage = null;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /** Add a blank row. */
    public void addRow() {
        rows.add(new Pair());
        int r = rows.size() - 1;
        fireTableRowsInserted(r, r);
    }

    /** Add a pair; returns false if key would duplicate an existing non-empty key. */
    public boolean addPair(String key, String value) {
        String k = key == null ? "" : key.trim();
        if (isDuplicateKey(k, -1)) {
            lastErrorMessage = "Duplicate key: \"" + k + "\"";
            Toolkit.getDefaultToolkit().beep();
            return false;
        }
        rows.add(new Pair(k, value));
        lastErrorMessage = null;
        int r = rows.size() - 1;
        fireTableRowsInserted(r, r);
        return true;
    }

    /** Remove a row by index (no-op if out of range). */
    public void removeRow(int row) {
        if (row < 0 || row >= rows.size()) return;
        rows.remove(row);
        fireTableRowsDeleted(row, row);
    }

    /** Clear all rows. */
    public void clearAll() {
        rows.clear();
        fireTableDataChanged();
    }

    /** Replace all data from a map. */
    public void setPairs(Map<String, String> map) {
        rows.clear();
        if (map != null) {
            for (Map.Entry<String, String> e : map.entrySet()) {
                rows.add(new Pair(e.getKey(), e.getValue()));
            }
        }
        lastErrorMessage = null;
        fireTableDataChanged();
    }

    /** Get data as an ordered map (skips empty keys). */
    public Map<String, String> toMap() {
        LinkedHashMap<String, String> out = new LinkedHashMap<>();
        for (Pair p : rows) {
            if (!p.key.isEmpty()) out.put(p.key, p.value);
        }
        return out;
    }

    /** Last validation error (useful to surface in UI), or null if none. */
    public String getLastErrorMessage() { return lastErrorMessage; }

    private boolean isDuplicateKey(String key, int ignoreRowIndex) {
        if (key.isEmpty()) return false; // allow multiple empty keys while editing
        for (int i = 0; i < rows.size(); i++) {
            if (i == ignoreRowIndex) continue;
            if (key.equals(rows.get(i).key)) return true;
        }
        return false;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(KeyValueTableModel::createAndShowUI);
    }

    private static void createAndShowUI() {
        JFrame frame = new JFrame("Key-Value Table Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        // Start with some initial data
        Map<String, String> initial = Map.of(
                "host", "localhost",
                "port", "5432"
        );
        KeyValueTableModel model = new KeyValueTableModel(initial);
        JTable table = new JTable(model);

        // buttons
        JButton add = new JButton("Add");
        add.addActionListener(e -> model.addRow());

        JButton del = new JButton("Delete");
        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) model.removeRow(r);
        });

        JButton clear = new JButton("Clear All");
        clear.addActionListener(e -> model.clearAll());

        JButton dump = new JButton("Dump to Console");
        dump.addActionListener(e -> {
            System.out.println("Current pairs: " + model.toMap());
        });

        // layout
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(add);
        buttons.add(del);
        buttons.add(clear);
        buttons.add(dump);

        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);

        // listen for errors
        model.addTableModelListener(ev -> {
            String err = model.getLastErrorMessage();
            if (err != null) {
                JOptionPane.showMessageDialog(frame, err, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    
}

