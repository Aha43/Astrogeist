package astrogeist.ui.swing.scanning;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import astrogeist.engine.abstraction.persistence.AstrogeistStorageManager;
import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

/**
 * <p>
 *   {@link Action} that shows the 
 *   {@link ScanningDialog} to perform a scan.
 * </p>
 */
public final class ShowScanningDialogAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final App app;
	private final AstrogeistStorageManager astrogeistStorageManager;
	private final Timeline timeline;
	private final TimelineValuePool tvp;
	
	public ShowScanningDialogAction(
		App app,
		AstrogeistStorageManager astrogeistStorageManager,
		Timeline timeline,
		TimelineValuePool tvp) {
		
		super("Scan"); 
		
		this.app = app;
		this.astrogeistStorageManager = astrogeistStorageManager;
		this.timeline = timeline;
		this.tvp = tvp;
	}
	
	@Override public final void actionPerformed(ActionEvent e) {
		try {
			var dlg = new ScanningDialog(this.app, this.astrogeistStorageManager, this.timeline, this.tvp);
			dlg.showIt();
		} catch (Exception ex) {
			MessageDialogs.showError(app.getFrame(), "Failed", ex);
		}
	}

}
