package astrogeist.ui.swing.scanning;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.jobs.DefaultJobRunner;
import astrogeist.engine.jobs.JobProgress;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.message.MessageDialogs;
import astrogeist.ui.swing.progress.JobProgressDialog;
import astrogeist.ui.swing.progress.JobToProgressAdapter;

public final class ScanAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final App app;
	
	private final ScannersSelectionPanel scannersSelectionPanel;
	
	private final Timeline timeline;
	
	public ScanAction(
		App app,
		Timeline timeline,
		ScannersSelectionPanel scannersSelectionPanel) {
		
		super("Scan");
		
		this.app = app;
		this.timeline = timeline;
		this.scannersSelectionPanel = scannersSelectionPanel;
	}

	@Override public final void actionPerformed(ActionEvent e) {
		try {
			var scanners = this.scannersSelectionPanel.getSelectedScanners();
			
	        this.timeline.clear();
	        
	        this.app.getSearchPanel().timelineView(this.timeline);
	        this.app.getTimelinePanel().timeline(this.timeline);
	        
	        // --- New: show progress dialog
	        var dlg = new JobProgressDialog(this.app.getFrame(), "Scanning…");
	        dlg.setVisible(true);
	        
	        // Create a runner (tune parallelism to your IO/CPU mix)
	        var runner = new DefaultJobRunner(2);
	
	        // Track completions so we can close dialog when all done
	        var allFutures = new ArrayList<CompletableFuture<Void>>();
	
	        for (var scanner : scanners) {
	            // Build a JobProgress row for UI
	            var jp = new JobProgress(scanner.getClass().getSimpleName())
	            		.setDescription(scanner.description());
	                  
	            dlg.addJob(jp);
	
	            var listener = new JobToProgressAdapter(jp, dlg.getPanel());
	            var handle = runner.submit(scanner, timeline, listener);
	            allFutures.add(handle.completion());
	        }
	
	        CompletableFuture
	            .allOf(allFutures.toArray(CompletableFuture[]::new))
	            .whenCompleteAsync((v, exAll) -> {
	            	this.app.getSearchPanel().timelineView(timeline);
	            	this.app.getTimelinePanel().timeline(timeline);
	                try { runner.close(); } catch (Exception ignore) {}
	            }, SwingUtilities::invokeLater);    
	        
		} catch (Exception x) {
			MessageDialogs.showError("Failed to scan", x);
		}
		
	}

}
