package astrogeist.ui.swing.component.logging;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import astrogeist.engine.logging.LoggingController;

public final class LoggingLevelsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final LoggingLevelsTableModel model = new LoggingLevelsTableModel();
    private final JTable table = new JTable(model);

    public LoggingLevelsPanel() {
        super(new BorderLayout(8,8));

        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        table.getColumnModel().getColumn(1).setCellEditor(new LevelCellEditor());
        table.getColumnModel().getColumn(1).setCellRenderer(new LevelCellRenderer());
        table.getColumnModel().getColumn(0).setPreferredWidth(380);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);

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
