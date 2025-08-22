package astrogeist.ui.swing.component.general;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;

/** Reusable panel for editing String→String pairs with add/delete/clear and "Enter adds row". */
public class KeyValuePairsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
	private final KeyValueTableModel model;
    private final JTable table;
    
    private final JButton addBtn = new JButton("Add");
    private final JButton delBtn = new JButton("Delete");
    private final JButton clearBtn = new JButton("Clear All");

    private final JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
    
    public KeyValuePairsPanel() { this(null); }

    public KeyValuePairsPanel(Map<String, String> initial) {
        super(new BorderLayout(8, 8));
        this.model = (initial == null) ? new KeyValueTableModel() : new KeyValueTableModel(initial);
        this.table = new JTable(model);

        // nice defaults
        this.table.setFillsViewportHeight(true);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        // Enter key: commit edit, add row if on last row, keep selection on same column
        installEnterAddsRowBehavior();

        // toolbar
        this.toolbar.add(addBtn);
        this.toolbar.add(delBtn);
        this.toolbar.add(clearBtn);

        super.add(new JScrollPane(table), BorderLayout.CENTER);
        super.add(toolbar, BorderLayout.SOUTH);

        // actions
        this.addBtn.addActionListener(e -> addRowAndFocus());
        this.delBtn.addActionListener(e -> removeSelectedRow());
        this.clearBtn.addActionListener(e -> model.clearAll());

        // surface errors as dialogs (optional; you can remove this if you prefer silent behavior)
        model.addTableModelListener(ev -> {
            if (ev.getType() == TableModelEvent.UPDATE || ev.getType() == TableModelEvent.INSERT) {
                String err = model.getLastErrorMessage();
                if (err != null) {
                    JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    protected final void addButton(JButton btn) { this.toolbar.add(btn); }

    private final void installEnterAddsRowBehavior() {
        var im = table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        var am = table.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "kv-enter");
        am.put("kv-enter", new AbstractAction() {
            private static final long serialVersionUID = 1L;

			@Override public void actionPerformed(ActionEvent e) {
                if (table.isEditing()) table.getCellEditor().stopCellEditing();
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (row == -1) {
                    addRowAndFocus();
                    return;
                }
                if (row == model.getRowCount() - 1) {
                    model.addRow();
                    row++;
                }
                // keep same column; move selection to the new/current row
                table.changeSelection(row, (col < 0 ? 0 : col), false, false);
                table.editCellAt(row, (col < 0 ? 0 : col));
                Component editor = table.getEditorComponent();
                if (editor != null) editor.requestFocusInWindow();
            }
        });
    }

    private final void addRowAndFocus() {
        model.addRow();
        int r = model.getRowCount() - 1;
        table.changeSelection(r, 0, false, false);
        table.editCellAt(r, 0);
        var editor = table.getEditorComponent();
        if (editor != null) editor.requestFocusInWindow();
    }

    private final void removeSelectedRow() {
        int r = table.getSelectedRow();
        if (r >= 0) model.removeRow(r);
    }

    // ------ Public API ------

    /** Replace all pairs (null allowed). */
    public final void setPairs(Map<String, String> map) { model.setPairs(map); }

    /** Returns pairs in insertion order (skips empty keys). */
    public final Map<String, String> getPairs() { return new LinkedHashMap<>(model.toMap()); }

    /** Expose table if caller wants more customization (column widths, renderers, etc.). */
    public final JTable getTable() { return table; }

    /** Enable/disable editing & buttons. */
    @Override public final void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        table.setEnabled(enabled);
        addBtn.setEnabled(enabled);
        delBtn.setEnabled(enabled);
        clearBtn.setEnabled(enabled);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(KeyValuePairsPanel::createAndShowUI);}
    
    private static void createAndShowUI() {
        JFrame frame = new JFrame("Key-Value Pairs Panel Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(560, 340);

        Map<String, String> initial = new LinkedHashMap<>();
        initial.put("host", "localhost");
        initial.put("port", "5432");

        KeyValuePairsPanel panel = new KeyValuePairsPanel(initial);

        JButton dumpBtn = new JButton("Dump to Console");
        dumpBtn.addActionListener(e -> System.out.println("Pairs = " + panel.getPairs()));

        JPanel top = new JPanel(new BorderLayout());
        top.add(panel, BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.add(dumpBtn);
        top.add(south, BorderLayout.SOUTH);

        frame.setContentPane(top);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
