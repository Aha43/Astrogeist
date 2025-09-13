package astrogeist.ui.swing.actions.scanning;

import astrogeist.engine.abstraction.Scanner;
import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.jobs.JobProgressListener;
import astrogeist.engine.abstraction.jobs.JobWorker;
import astrogeist.engine.async.CancellationToken;

/** Wraps the legacy Scanner into a JobWorker. */
public final class LegacyScannerWorker implements JobWorker<LegacyScannerWorker.Input> {

    public static final class Input {
        public final Scanner scanner;
        public final Timeline timeline;
        public final String displayName;   // e.g. "Seestar Scanner"
        public final String description;   // e.g. "Scan Seestar output"
        public Input(Scanner scanner, Timeline timeline) {
            this.scanner = scanner;
            this.timeline = timeline;
            this.displayName = scanner.name();
            this.description = scanner.description();
        }
    }

    @Override public String name() { return "LegacyScanner"; }
    @Override public String description() { return "Runs existing Scanner.scan(Timeline)"; }

    @Override public void run(Input input, JobProgressListener listener, CancellationToken token) throws Exception {
        // We don't have per-file progress in the legacy API,
        // so report unknown total and a couple of status messages.
        listener.onStart(-1);
        listener.onMessage("Starting " + input.displayName + "…");
        try {
            // If you add cancellation checks into your scanner implementations later,
            // they can poll token.isCancelled() via some shared context.
            input.scanner.scan(input.timeline);
            listener.onSuccess(input.displayName, "Completed.");
        } catch (Throwable ex) {
            listener.onFailure(input.displayName, ex);
            throw ex;
        } finally {
            listener.onDone();
        }
    }
    
}
