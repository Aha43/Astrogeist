package astrogeist.ui.swing.actions.scanning;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;

import astrogeist.engine.abstraction.Scanner;
import astrogeist.engine.abstraction.TimelineValuePool;
import astrogeist.engine.jobs.DefaultJobRunner;
import astrogeist.engine.jobs.JobProgress;
import astrogeist.engine.resources.Resources;
import astrogeist.engine.scanner.ScannerConfigLoader;
import astrogeist.engine.scanner.userdata.UserDataScanner;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.message.MessageDialogs;
import astrogeist.ui.swing.progress.JobProgressDialog;
import astrogeist.ui.swing.progress.JobToProgressAdapter;
import astrogeist.ui.swing.scanning.ScanningDialog;

/**
 * <p>
 *   {@link Action} that perform a scan.
 * </p>
 */
public final class ScanAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final App app;
	
	public ScanAction(App app) { super("Scan"); this.app = app; }
	
	@Override public final void actionPerformed(ActionEvent e) {
		try {
			var dlg = new ScanningDialog(app);
			dlg.showIt();
		} catch (Exception ex) {
			MessageDialogs.showError(app.getFrame(), "Failed", ex);
		}
	}
	
//	public final void actionPerformedold(ActionEvent e) {
//	    try {
//	        var timeline = this.app.getServices().getTimeline();
//	        timeline.clear();
//
//	        var scanners = loadScanners(this.app.getServices().getTimelineValuePool()); // List<Scanner>
//
//	        // Keep your existing bindings
//	        this.app.getSearchPanel().timelineView(timeline);
//	        this.app.getTimelinePanel().timeline(timeline);
//
//	        // --- New: show progress dialog
//	        var dlg = new JobProgressDialog(this.app.getFrame(), "Scanning…");
//	        dlg.setVisible(true);
//
//	        // Create a runner (tune parallelism to your IO/CPU mix)
//	        var runner = new DefaultJobRunner(2);
//
//	        // Track completions so we can close dialog when all done
//	        var allFutures = new ArrayList<CompletableFuture<Void>>();
//
//	        for (var scanner : scanners) {
//	            // Build a JobProgress row for UI
//	            var jp = new JobProgress(scanner.getClass().getSimpleName())
//	            		.setDescription(scanner.description());
//	                  
//	            dlg.addJob(jp);
//
//	            var listener = new JobToProgressAdapter(jp, dlg.getPanel());
//	            var handle = runner.submit(scanner, timeline, listener);
//	            allFutures.add(handle.completion());
//	        }
//
//	        // Optional: close dialog when *all* jobs complete (success or error)
//	        CompletableFuture
//	            .allOf(allFutures.toArray(CompletableFuture[]::new))
//	            .whenCompleteAsync((v, exAll) -> {
//	                
//	            	this.app.getSearchPanel().timelineView(timeline);
//	            	this.app.getTimelinePanel().timeline(timeline);
//	                try { runner.close(); } catch (Exception ignore) {}
//	            }, SwingUtilities::invokeLater);
//
//	    } catch (Exception ex) {
//	        MessageDialogs.showError("Failed to scan", ex);
//	    }
//	}
//
//	private static final List<Scanner> loadScanners(TimelineValuePool tvp) throws Exception {
//		var configFile = Resources.getScanningConfigFile();
//		var config = ScannerConfigLoader.parse(configFile);
//		var retVal = ScannerConfigLoader.createScanners(config);
//		var userScanner = new UserDataScanner(tvp);
//		retVal.add(userScanner);
//		return retVal;
//	}

}
