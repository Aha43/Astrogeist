package aha.common.ui.swing.diagnostic;

import java.awt.Frame;

import javax.swing.JMenu;

public final class LoggingMenu extends JMenu {
	private static final long serialVersionUID = 1L;

	public LoggingMenu(Frame parent) {
		super("Logging");
		super.add(new GlobalLoggingControlAction(parent));
		super.add(new PackageLoggingControlAction(parent));
	}

}
