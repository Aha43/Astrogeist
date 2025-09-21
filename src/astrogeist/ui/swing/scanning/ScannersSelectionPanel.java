package astrogeist.ui.swing.scanning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import astrogeist.engine.abstraction.Scanner;

/**
 * <p>
 *   A panel that lists loaded scanners and lets the user choose which to run.
 * </p>
 */
public final class ScannersSelectionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
    
	private final JTable table = new JTable();
    private final Model model = new Model();
    private final EventListenerList listeners = new EventListenerList();

    public ScannersSelectionPanel() {
        super(new BorderLayout(8,8));
        table.setModel(model);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(22);

        // Nice checkbox editor for column 0
        table.getColumnModel().getColumn(0).setMaxWidth(64);
        table.getColumnModel().getColumn(0).setMinWidth(64);
        table.getColumnModel().getColumn(0).setPreferredWidth(64);

        // Red-row renderer for rows where canScan==false
        var redRowRenderer = new RedRowRenderer(model);
        table.setDefaultRenderer(Object.class, redRowRenderer);
        table.setDefaultRenderer(String.class, redRowRenderer);
        // (Boolean column uses built-in renderer; we’ll also paint row bg via table.prepareRenderer)
        table.setDefaultRenderer(Boolean.class, new BooleanWithRowColorRenderer(redRowRenderer));
        
        super.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // ---- Public API ---------------------------------------------------------
    
    /**
     * <p>
     *   Replace the list of scanners shown.
     * </p>
     * @param scanners {@link Scanner}s.
     */
    public final void setScanners(List<Scanner> scanners) {
        SwingUtilities.invokeLater(() -> { model.setScanners(scanners); }); }
    
    /**
     * <p>
     *   Re-check canScan() for all scanners (use after user mounted a disk etc.).
     * </p>
     */
    public final void refreshCanScan() {
        SwingUtilities.invokeLater(() -> { model.refreshCanScan(); fireChanged(); }); }

    /**
     * <p>
     *   Return currently selected scanners that can scan right now.  
     * </p>
     * @return
     */
    public final List<Scanner> getSelectedScanners() { return model.getSelectedScanners(); }
    
    /**
     * <p>
     *   Add listener to be notified when a selection changes.
     * </p>
     * @param l {@link ChangeListener} to add.
     */
    public final void addChangeListener(ChangeListener l) { listeners.add(ChangeListener.class, l); }
    
    /**
     * <p>
     *   Remove listener to be notified when a selection changes.
     * </p>
     * @param l {@link ChangeListener} to remove.
     */
    public final void removeChangeListener(ChangeListener l) { listeners.remove(ChangeListener.class, l); }

    private final ChangeEvent changeEvent = new ChangeEvent(this);
    private final void fireChanged() {
        var ls = listeners.getListeners(ChangeListener.class);
        for (var l : ls) l.stateChanged(this.changeEvent);
    }

    // ---- Table model --------------------------------------------------------

    private static final class Row {
        final Scanner scanner;
        boolean selected;   // user checkbox
        boolean canScan;    // derived from scanner.canScan()

        Row(Scanner scanner) {
            this.scanner = Objects.requireNonNull(scanner);
            this.canScan = safeCanScan(scanner);
            this.selected = this.canScan; // default: selected if possible
        }

        static final boolean safeCanScan(Scanner s) {
            try { return s.canScan(); }
            catch (Throwable t) { return false; }
        }
    }

    private final class Model extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        
		private final List<Row> rows = new ArrayList<>();
        private final String[] cols = {"Use", "Scanner", "Description"};

        final void setScanners(List<Scanner> scanners) {
            rows.clear();
            if (scanners != null) {
                for (var sc : scanners) rows.add(new Row(sc));
            }
            fireTableDataChanged();
        }

        final void refreshCanScan() {
            boolean anyChanged = false;
            for (var r : rows) {
                boolean before = r.canScan;
                r.canScan = Row.safeCanScan(r.scanner);
                if (!r.canScan) r.selected = false;          // auto-unselect when not available
                else if (!before && r.canScan) r.selected = true; // reselect when becomes available
                anyChanged |= (before != r.canScan);
            }
            if (anyChanged) fireTableDataChanged();
            else fireTableRowsUpdated(0, Math.max(0, rows.size()-1));
        }

        final List<Scanner> getSelectedScanners() {
            var out = new ArrayList<Scanner>();
            for (var r : rows) if (r.selected && r.canScan) out.add(r.scanner);
            return out;
        }

        @SuppressWarnings("unused")
		final Row rowAt(int viewRow) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            return (modelRow >= 0 && modelRow < rows.size()) ? rows.get(modelRow) : null;
        }

        @Override public final int getRowCount() { return rows.size(); }
        @Override public final int getColumnCount() { return cols.length; }
        @Override public final String getColumnName(int c) { return cols[c]; }

        @Override public final Class<?> getColumnClass(int c) {
            return switch (c) {
                case 0 -> Boolean.class;
                default -> String.class;
            };
        }

        @Override public final boolean isCellEditable(int rowIndex, int columnIndex) {
            Row r = rows.get(rowIndex);
            if (columnIndex == 0) {
                // Checkbox is editable only when canScan == true
                return r.canScan;
            }
            return false;
        }

        @Override public final Object getValueAt(int rowIndex, int columnIndex) {
            Row r = rows.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> r.selected;
                case 1 -> safeString(r.scanner.name());
                case 2 -> safeString(r.scanner.description());
                default -> "";
            };
        }

        @Override public final void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0 && rowIndex >= 0 && rowIndex < rows.size()) {
                Row r = rows.get(rowIndex);
                boolean newVal = Boolean.TRUE.equals(aValue);
                if (r.canScan) {
                    r.selected = newVal;
                    fireTableRowsUpdated(rowIndex, rowIndex);
                    fireChanged(); // notify external listeners
                }
            }
        }

        private String safeString(String s) { return s == null ? "" : s; }
    }

    // ---- Renderers to color rows with canScan=false -------------------------

    /** Colors entire row red (slightly translucent) when canScan==false. */
    private static final class RedRowRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        
		private final Model model;
        private final Color unavailableBg = new Color(255, 64, 64, 40); // light red
        private final Color unavailableSelBg = new Color(255, 64, 64, 90);

        RedRowRenderer(Model model) { this.model = model; }

        @Override public final Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Determine model row
            int modelRow = table.convertRowIndexToModel(row);
            boolean canScan = true;
            if (modelRow >= 0 && modelRow < model.rows.size()) {
                canScan = model.rows.get(modelRow).canScan;
            }

            if (!canScan) {
                c.setBackground(isSelected ? unavailableSelBg : unavailableBg);
                if (c instanceof JComponent jc) {
                    jc.setToolTipText("Unavailable (see description)");
                }
            } else {
                // restore normal background (important when reusing renderers)
                c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                if (c instanceof JComponent jc) jc.setToolTipText(null);
            }
            return c;
        }
    }

    /** Ensures Boolean renderer also uses the row coloring. */
    private static final class BooleanWithRowColorRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        
		private final RedRowRenderer rowRenderer;

        BooleanWithRowColorRenderer(RedRowRenderer rowRenderer) {
            this.rowRenderer = rowRenderer;
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override public final Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            // reuse red-row coloring; then set a checkbox-like text
            Component c = rowRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (c instanceof JLabel lbl) {
                lbl.setText(Boolean.TRUE.equals(value) ? "✔" : ""); // simple visual; editor is the real checkbox
            }
            return c;
        }
    }

}
