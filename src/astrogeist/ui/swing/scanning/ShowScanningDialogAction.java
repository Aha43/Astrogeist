package astrogeist.ui.swing.scanning;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

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
	
	public ShowScanningDialogAction(App app) { super("Scan"); this.app = app; }
	
	@Override public final void actionPerformed(ActionEvent e) {
		try {
			var dlg = new ScanningDialog(app);
			dlg.showIt();
		} catch (Exception ex) {
			MessageDialogs.showError(app.getFrame(), "Failed", ex);
		}
	}

}
