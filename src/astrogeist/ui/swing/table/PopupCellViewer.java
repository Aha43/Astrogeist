package astrogeist.ui.swing.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import astrogeist.Common;

public final class PopupCellViewer {
    private PopupCellViewer() { Common.throwStaticClassInstantiateError(); }

    /** Install a right-click popup that shows the full cell content for the given view-column indices. */
    public final static void install(JTable table, int... viewCols) {
        final Set<Integer> allowed = IntStream.of(viewCols).boxed().collect(Collectors.toSet());

        table.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { maybeShow(e); }
            @Override public void mouseReleased(MouseEvent e) { maybeShow(e); }

            private void maybeShow(MouseEvent e) {
                if (!e.isPopupTrigger()) return;

                int viewRow = table.rowAtPoint(e.getPoint());
                int viewCol = table.columnAtPoint(e.getPoint());
                if (viewRow < 0 || viewCol < 0 || !allowed.contains(viewCol)) return;

                table.setRowSelectionInterval(viewRow, viewRow);
                table.setColumnSelectionInterval(viewCol, viewCol);

                int modelRow = table.convertRowIndexToModel(viewRow);
                int modelCol = table.convertColumnIndexToModel(viewCol);
                Object value = table.getModel().getValueAt(modelRow, modelCol);
                showPopup(table, e.getX(), e.getY(), value);
            }
        });

        // Optional: keyboard shortcut (ENTER or SPACE to open on selected cell)
        InputMap im = table.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = table.getActionMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), "popupCellViewer");
        im.put(KeyStroke.getKeyStroke("SPACE"), "popupCellViewer");
        am.put("popupCellViewer", new AbstractAction() {
            private static final long serialVersionUID = 1L;

			@Override public void actionPerformed(java.awt.event.ActionEvent e) {
                int viewRow = table.getSelectedRow();
                int viewCol = table.getSelectedColumn();
                if (viewRow < 0 || viewCol < 0 || !allowed.contains(viewCol)) return;
                int modelRow = table.convertRowIndexToModel(viewRow);
                int modelCol = table.convertColumnIndexToModel(viewCol);
                Object value = table.getModel().getValueAt(modelRow, modelCol);

                Rectangle r = table.getCellRect(viewRow, viewCol, true);
                showPopup(table, r.x, r.y + r.height, value);
            }
        });
    }

    private final static void showPopup(JTable table, int x, int y, Object value) {
        var popup = new JPopupMenu();

        var text = new JTextArea();
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        text.setText(value == null ? "" : String.valueOf(value));
        text.setCaretPosition(0);

        var scroll = new JScrollPane(text);
        scroll.setPreferredSize(preferredSizeFor(text.getText(), table));

        var copy = new JButton("Copy");
        copy.addActionListener(ev -> {
            String s = text.getText();
            var sel = new StringSelection(s);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
        });

        var content = new JPanel(new BorderLayout(8,8));
        content.add(scroll, BorderLayout.CENTER);
        var south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        south.add(copy);
        content.add(south, BorderLayout.SOUTH);

        popup.setLayout(new BorderLayout());
        popup.add(content, BorderLayout.CENTER);
        popup.show(table, x, y);
    }

    private final static Dimension preferredSizeFor(String s, JComponent anchor) {
        // Size heuristics: max ~600x300, scale width with content length
        int lines = s == null ? 1 : Math.min(10, Math.max(1, s.split("\\R").length));
        int width = Math.min(600, Math.max(300, s == null ? 300 : Math.min(600, 8 * Math.min(80, s.length()))));
        int height = Math.min(300, 24 + lines * 18);
        return new Dimension(width, height);
    }
    
}
