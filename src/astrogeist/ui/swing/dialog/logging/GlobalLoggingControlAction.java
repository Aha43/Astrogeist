package astrogeist.ui.swing.dialog.logging;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import astrogeist.ui.swing.App;

public final class GlobalLoggingControlAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final App app;
	
	public GlobalLoggingControlAction(App app) { 
		super("Global logging...");
		this.app = app;
	}

	@Override public void actionPerformed(ActionEvent e) { GlobalLoggingControlDialog.showDialog(app); }
}
