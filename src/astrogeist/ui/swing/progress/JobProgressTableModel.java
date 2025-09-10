package astrogeist.ui.swing.progress;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public final class JobProgressTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

	private final List<JobProgress> rows = new ArrayList<>();

    private static final String[] COLS = {
        "Name", "Status", "Progress", "OK", "Failed", "Root", "Description"
    };

    public void setJobs(List<JobProgress> jobs) {
        rows.clear();
        rows.addAll(jobs);
        fireTableDataChanged();
    }

    public void addJob(JobProgress job) {
        rows.add(job);
        fireTableRowsInserted(rows.size()-1, rows.size()-1);
    }

    public JobProgress getAt(int row) {
        return (row >= 0 && row < rows.size()) ? rows.get(row) : null;
    }

    public void jobUpdated(JobProgress job) {
        int idx = rows.indexOf(job);
        if (idx >= 0) fireTableRowsUpdated(idx, idx);
        else fireTableDataChanged();
    }

    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return COLS.length; }
    @Override public String getColumnName(int c) { return COLS[c]; }

    @Override public Class<?> getColumnClass(int c) {
        return switch (c) {
            case 2, 3, 4 -> Integer.class;
            default -> String.class;
        };
    }

    @Override public Object getValueAt(int r, int c) {
        JobProgress jp = rows.get(r);
        return switch (c) {
            case 0 -> jp.getName();
            case 1 -> jp.getStatus().name();
            case 2 -> jp.getPercent();
            case 3 -> jp.getOkCount();
            case 4 -> jp.getFailCount();
            case 5 -> jp.getRootInfo();
            case 6 -> jp.getDescription();
            default -> "";
        };
    }
}

