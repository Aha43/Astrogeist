package astrogeist.ui.swing.progress;

import java.util.List;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

import astrogeist.engine.jobs.JobProgress;

public final class Demo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var dlg = new JobProgressDialog(null, "Jobs Progress");
            dlg.setVisible(true);

            var scan1 = new JobProgress("Seestar Scanner")
                .setDescription("Scan Seestar output")
                .setRootInfo("/astro/Seestar");
            var scan2 = new JobProgress("SharpCap Scanner")
                .setDescription("Scan SharpCap sessions")
                .setRootInfo("/astro/SharpCap");

            dlg.setJobs(List.of(scan1, scan2));

            var uiPanel = dlg.getPanel();

            var pool = Executors.newFixedThreadPool(2);
            pool.submit(() -> runFakeJob(scan1, uiPanel));
            pool.submit(() -> runFakeJob(scan2, uiPanel));
        });
    }

    static void runFakeJob(JobProgress jp, JobsProgressPanel ui) {
        jp.start(); ui.refreshJob(jp);
        int total = 120;
        for (int i=1; i<=total; i++) {
            try { Thread.sleep(25); } catch (InterruptedException ignored) {}
            // pretend: 1 out of 15 fails
            if (i % 15 == 0) jp.incFail(1); else jp.incOk(1);
            int pct = (int)Math.round(100.0 * i / total);
            jp.setPercent(pct);
            jp.message("Processed " + i + " / " + total + " files...");
            ui.refreshJob(jp);
        }
        jp.message("Summary: ok=" + jp.getOkCount() + ", failed=" + jp.getFailCount());
        jp.complete();
        ui.refreshJob(jp);
    }
}
