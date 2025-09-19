package astrogeist.ui.swing.progress;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellRenderer;

import astrogeist.engine.jobs.JobProgress;
import astrogeist.ui.swing.table.ColumnTooltipEnabler;
import astrogeist.ui.swing.table.PopupCellViewer;

/**
 * <p>
 *   Panel that shows the state of 
 *   {@link JobProgress} objects, that is progress of 'jobs'.
 * </p>
 */
public final class JobsProgressPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
	private final JobProgressTableModel model = new JobProgressTableModel();
    private final JTable table = new JTable(model);
    private final JTextArea detailsArea = new JTextArea(7, 40);

    public JobsProgressPanel() {
        super(new BorderLayout(8,8));

        this.table.setAutoCreateRowSorter(true);
        this.table.setFillsViewportHeight(true);
        
        ColumnTooltipEnabler.enable(this.table, 5);
        PopupCellViewer.install(table, 1, 2, 3, 4, 5);

        // Simple progress renderer for "Progress" column
        TableCellRenderer progressRenderer = (tbl, value, isSel, hasFocus, row, col) -> {
            int v = (value instanceof Integer) ? (Integer)value : 0;
            JProgressBar bar = new JProgressBar(0,100);
            bar.setValue(v);
            bar.setStringPainted(true);
            if (isSel) bar.setBackground(tbl.getSelectionBackground());
            return bar;
        };
        this.table.getColumnModel().getColumn(2).setCellRenderer(progressRenderer);
        this.table.setRowHeight(22);

        this.detailsArea.setEditable(false);
        this.detailsArea.setLineWrap(true);
        this.detailsArea.setWrapStyleWord(true);

        this.table.getSelectionModel().addListSelectionListener(this::onSelectionChanged);

        var split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        var top = new JScrollPane(table);
        top.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createTitledBorder("Progress"),   // your title here
        	    BorderFactory.createEmptyBorder(4, 8, 8, 8)    // padding inside the border
        	));
        
        var bottom = new JScrollPane(detailsArea);
        bottom.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createTitledBorder("Details"),   // your title here
        	    BorderFactory.createEmptyBorder(4, 8, 8, 8)    // padding inside the border
        	));
        
        split.setResizeWeight(0.66);           // weight for top component when resizing
        split.setDividerLocation(0.66);
        split.setTopComponent(top);
        split.setBottomComponent(bottom);
        super.add(split, BorderLayout.CENTER);
        
        //bottom.setBorder(BorderFactory.createTitledBorder("Details"));

        //add(top, BorderLayout.CENTER);
        //add(bottom, BorderLayout.SOUTH);
    }

    private final void onSelectionChanged(ListSelectionEvent e) {
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

    private final String buildDetails(JobProgress jp) {
        if (jp == null) return "";
        var sb = new StringBuilder();
        sb.append("Name: ").append(jp.getName()).append('\n');
        sb.append("Status: ").append(jp.getStatus()).append('\n');
        sb.append("Progress: ").append(jp.getPercent()).append("%\n");
        if (jp.getDescription() != null) sb.append("Description: ").append(jp.getDescription()).append('\n');
        sb.append("Succeeded: ").append(jp.getOkCount())
          .append("  Failed: ").append(jp.getFailCount()).append("\n\n");
        if (jp.getDetails() != null && !jp.getDetails().isBlank())
          sb.append(jp.getDetails()).append('\n');
        return sb.toString();
    }

    // API

    public final void setJobs(List<JobProgress> jobs) {
        SwingUtilities.invokeLater(() -> model.setJobs(jobs)); }

    public final void addJob(JobProgress job) {
        SwingUtilities.invokeLater(() -> model.addJob(job)); }

    public final void refreshJob(JobProgress job) {
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
