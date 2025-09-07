package astrogeist.ui.swing.actions.scanning;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import astrogeist.engine.resources.Resources;
import astrogeist.engine.scanner.CompositeScanner;
import astrogeist.engine.scanner.ScannerConfigLoader;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

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
			var timeline = this.app.getServices().getTimeline();
			timeline.clear();
			var tvp = this.app.getServices().getTimelineValuePool();
			var compositeScanner = new CompositeScanner(tvp);
			loadScanners(compositeScanner);
			compositeScanner.scan(timeline);
			this.app.getSearchPanel().timelineView(timeline);
			this.app.getTimelinePanel().timeline(timeline);
		} catch (Exception ex) {
			MessageDialogs.showError("Failed to scan", ex);
		}
	}
	
	private static final void loadScanners(CompositeScanner compositeScanner) throws Exception {
		var configFile = Resources.getScanningConfigFile();
		var config = ScannerConfigLoader.parse(configFile);
		ScannerConfigLoader.buildScanner(config, compositeScanner);
	}

}
