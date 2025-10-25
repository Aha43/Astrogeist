package astrogeist.ui.swing.dialog.logging;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import astrogeist.ui.swing.App;

public final class PackageLoggingControlAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final App app;
	
	public PackageLoggingControlAction(App app) { 
		super("Logging by Package..."); 
		this.app = app;
	}

	@Override public void actionPerformed(ActionEvent e) { PackageLoggingControlDialog.show(app); }
}
