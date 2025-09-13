package astrogeist.ui.swing.progress;

import javax.swing.SwingUtilities;

import astrogeist.engine.abstraction.jobs.JobProgressListener;
import astrogeist.engine.jobs.JobProgress;

public final class JobToProgressAdapter implements JobProgressListener {
	private final JobProgress job;
    private final JobsProgressPanel ui;

    private int total = -1;
    private int done = 0;

    public JobToProgressAdapter(JobProgress job, JobsProgressPanel ui) {
        this.job = job;
        this.ui = ui;
    }

    @Override public final void onStart(int total) {
        this.total = total;
        job.start();
             if (total == 0) job.setPercent(100);
        else if (total < 0)  job.message("Starting… (unknown total)");
        else                 job.message("Starting… total=" + total);
        
        ui.refreshJob(job);
    }

    @Override public final void onSuccess(Object item, String info) {
        done++;
        job.incOk(1);
        updateProgress("OK: " + (info != null ? info : item.toString()));
    }

    @Override public final void onFailure(Object item, Throwable error) {
        done++;
        job.incFail(1);
        String msg = "FAIL: " + item + " — " + (error == null ? "unknown error" : error.getMessage());
        appendDetails(msg);
        updateProgress(msg);
    }

    @Override public final void onMessage(String message) {
        appendDetails(message);
        SwingUtilities.invokeLater(() -> ui.refreshJob(job));
    }

    @Override public final void onDone() {
        job.message(summaryText());
        job.complete();
        ui.refreshJob(job);
    }

    private final void updateProgress(String lastMsg) {
        if (total > 0) {
            int pct = (int)Math.round(100.0 * done / total);
            job.setPercent(pct);
        }
        appendDetails(lastMsg);
        SwingUtilities.invokeLater(() -> ui.refreshJob(job));
    }

    private final void appendDetails(String line) {
        String prev = job.getDetails();
        if (prev == null || prev.isBlank()) job.message(line);
        else job.message(prev + System.lineSeparator() + line);
    }

    private final String summaryText() {
        StringBuilder sb = new StringBuilder();
        sb.append("Summary: ok=").append(job.getOkCount())
          .append(", failed=").append(job.getFailCount());
        if (total > 0) sb.append(", total=").append(total);
        return sb.toString();
    }

}
