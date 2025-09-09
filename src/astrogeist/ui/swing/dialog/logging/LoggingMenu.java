package astrogeist.ui.swing.dialog.logging;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import astrogeist.ui.swing.App;

public final class LoggingMenu extends JMenu {
	private static final long serialVersionUID = 1L;

	public LoggingMenu(App app) {
		super("Logging");
		
		var menuItem = new JMenuItem("Global logging...");
		menuItem.addActionListener(e -> LoggingControlDialog.show(app));
		add(menuItem);
		
		menuItem = new JMenuItem("Logging by Package...");
		menuItem.addActionListener(e -> PackageLoggingDialog.show(app));
		add(menuItem);
	}

}
