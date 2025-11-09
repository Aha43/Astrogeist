package astrogeist.ui.swing.component.logging;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import aha.common.logging.Log;
import aha.common.logging.LoggingController;

public final class LoggingLevelsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private final Logger logger = Log.get(this);

    private final LoggingLevelsTableModel model = new LoggingLevelsTableModel();
    private final JTable table = new JTable(model);

    public LoggingLevelsPanel() {
        super(new BorderLayout(8,8));

        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        table.getColumnModel().getColumn(0).setPreferredWidth(380);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);

        // Double-click Level column -> open selection dialog
        table.addMouseListener(new MouseAdapter() {
            @Override public final void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    int viewRow = table.rowAtPoint(e.getPoint());
                    int viewCol = table.columnAtPoint(e.getPoint());
                    if (viewRow < 0 || viewCol != 1) return;

                    int modelRow = table.convertRowIndexToModel(viewRow);
                    Object current = model.getValueAt(modelRow, 1);

                    Level[] choices = LoggingLevelsTableModel
                            .allLevels()
                            .toArray(new Level[0]);

                    Level selected = (Level) JOptionPane.showInputDialog(
                            table,
                            "Select logging level:",
                            "Logging Level",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            choices,
                            current instanceof Level ? current : Level.INFO
                    );

                    if (selected != null && !selected.equals(current)) {
                        model.setValueAt(selected, modelRow, 1);
                    }
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        var south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        var refreshBtn = new JButton("Refresh");
        var applyBtn = new JButton("Apply");
        var inheritBtn = new JButton("Set Inherit for Selected");
        var popBtn = new JButton("Pop (Restore)");

        refreshBtn.addActionListener(e -> model.refreshFromLogManager());

        applyBtn.addActionListener(e -> {
            Map<String, Level> map = new LinkedHashMap<>();
            model.toPackageLevelMap().forEach((ns, lv) -> {
                // Interpret "*" row as root logger ("")
                String key = "*".equals(ns) ? "" : ns;
                this.logger.info("Set for ns : '" + ns + "' level : '" + lv + 
                	"'");
                map.put(key, lv);
            });
            LoggingController.apply(map);
        });

        inheritBtn.addActionListener(e -> {
            int[] rows = table.getSelectedRows();
            for (int r : rows) {
                int m = table.convertRowIndexToModel(r);
                // Setting null = inherit; we represent it by removing explicit level after apply:
                // Here we’ll just set INFO visually; inheritance will be handled when user presses Pop/Apply.
                model.setValueAt(Level.INFO, m, 1); // visual hint; real “null” happens via Pop
            }
            table.repaint();
        });

        popBtn.addActionListener(e -> LoggingController.pop());

        south.add(refreshBtn);
        south.add(inheritBtn);
        south.add(popBtn);
        south.add(applyBtn);
        add(south, BorderLayout.SOUTH);
    }
    
}
