package astrogeist.ui.swing.scanning;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.formdev.flatlaf.extras.FlatSVGIcon;

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
	
	public ShowScanningDialogAction(App app) { 
		 // must NOT be null
		
		
		super("Scan"); 
		
		this.app = app;
		
		var url = "/astrogeist/engine/resources/activity.svg";
		var urlf = this.getClass().getResource(url);
		System.out.println("SVG URL = " + urlf);
		
		var icon = new FlatSVGIcon(url, 16, 16);
		//this.putValue(SMALL_ICON, icon);
		
		 // adjust path
		
	}
	
	@Override public final void actionPerformed(ActionEvent e) {
		try {
			var dlg = new ScanningDialog(app);
			dlg.showIt();
		} catch (Exception ex) {
			MessageDialogs.showError(app.getFrame(), "Failed", ex);
		}
	}

}
