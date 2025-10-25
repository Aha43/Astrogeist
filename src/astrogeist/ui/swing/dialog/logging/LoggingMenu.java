package astrogeist.ui.swing.dialog.logging;

import javax.swing.JMenu;

import astrogeist.ui.swing.App;

public final class LoggingMenu extends JMenu {
	private static final long serialVersionUID = 1L;

	public LoggingMenu(App app) {
		super("Logging");
		super.add(new GlobalLoggingControlAction(app));
		super.add(new PackageLoggingControlAction(app));
	}

}
