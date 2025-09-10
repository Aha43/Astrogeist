package astrogeist.ui.swing.progress;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public final class JobsProgressPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
	private final JobProgressTableModel model = new JobProgressTableModel();
    private final JTable table = new JTable(model);
    private final JProgressBar rowProgress = new JProgressBar(0, 100);
    private final JTextArea detailsArea = new JTextArea(7, 40);

    public JobsProgressPanel() {
        super(new BorderLayout(8,8));

        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);

        // Simple progress renderer for "Progress" column
        TableCellRenderer progressRenderer = (tbl, value, isSel, hasFocus, row, col) -> {
            int v = (value instanceof Integer) ? (Integer)value : 0;
            JProgressBar bar = new JProgressBar(0,100);
            bar.setValue(v);
            bar.setStringPainted(true);
            if (isSel) bar.setBackground(tbl.getSelectionBackground());
            return bar;
        };
        table.getColumnModel().getColumn(2).setCellRenderer(progressRenderer);
        table.setRowHeight(22);

        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);

        table.getSelectionModel().addListSelectionListener(this::onSelectionChanged);

        var top = new JScrollPane(table);
        var bottom = new JScrollPane(detailsArea);
        bottom.setBorder(BorderFactory.createTitledBorder("Details"));

        add(top, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void onSelectionChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = table.getSelectedRow();
        if (row >= 0) {
            int modelRow = table.convertRowIndexToModel(row);
            JobProgress jp = model.getAt(modelRow);
            detailsArea.setText(buildDetails(jp));
            detailsArea.setCaretPosition(0);
        } else {
            detailsArea.setText("");
        }
    }

    private String buildDetails(JobProgress jp) {
        if (jp == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(jp.getName()).append('\n');
        sb.append("Status: ").append(jp.getStatus()).append('\n');
        sb.append("Progress: ").append(jp.getPercent()).append("%\n");
        if (jp.getRootInfo() != null) sb.append("Root: ").append(jp.getRootInfo()).append('\n');
        if (jp.getDescription() != null) sb.append("Description: ").append(jp.getDescription()).append('\n');
        sb.append("Succeeded: ").append(jp.getOkCount())
          .append("  Failed: ").append(jp.getFailCount()).append("\n\n");
        if (jp.getDetails() != null && !jp.getDetails().isBlank()) {
            sb.append(jp.getDetails()).append('\n');
        }
        return sb.toString();
    }

    // API

    public void setJobs(List<JobProgress> jobs) {
        SwingUtilities.invokeLater(() -> model.setJobs(jobs));
    }

    public void addJob(JobProgress job) {
        SwingUtilities.invokeLater(() -> model.addJob(job));
    }

    public void refreshJob(JobProgress job) {
        SwingUtilities.invokeLater(() -> {
            model.jobUpdated(job);
            // keep details synced if selected
            int row = table.getSelectedRow();
            if (row >= 0) {
                int modelRow = table.convertRowIndexToModel(row);
                if (model.getAt(modelRow) == job) {
                    detailsArea.setText(buildDetails(job));
                }
            }
        });
    }
}

