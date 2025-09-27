package astrogeist.ui.swing.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import astrogeist.Common;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.about.AboutDialog;
import astrogeist.ui.swing.dialog.logging.LoggingMenu;
import astrogeist.ui.swing.dialog.settings.SettingsDialog;
import astrogeist.ui.swing.tool.sun.sketching.ShowSunDialogAction;

public final class MenuBarFactory {
	private MenuBarFactory() { Common.throwStaticClassInstantiateError(); }
	
	public static JMenuBar createMenuBar(App app, TimelineNames timelineNames) {
		var menuBar = new JMenuBar();
		menuBar.add(createAstrogeistMenu(app, timelineNames));
		menuBar.add(createDiagnosticMenu(app));
		menuBar.add(createVisualMenu(app));
		menuBar.add(createHelpMenu(app));
		return menuBar;
	}
	
	private static JMenu createAstrogeistMenu(App app, TimelineNames timelineNames) {
		var retVal = new JMenu("File");
		retVal.add(createSettingsItem(app, timelineNames));
		retVal.add(createExitItem());
		return retVal;
	}
	
	private static JMenuItem createSettingsItem(App app, TimelineNames timelineNames) {
		var retVal = new JMenuItem("Settings");
		retVal.addActionListener(e -> SettingsDialog.show(app, timelineNames));
		return retVal;
	}
	
	private static JMenuItem createExitItem() { 
		var retVal = new JMenuItem("Exit");
		retVal.addActionListener(e -> System.exit(0));
		return retVal;
	}
	
	private static JMenu createDiagnosticMenu(App app) {
		var retVal = new JMenu("Diagnostic");
		retVal.add(new LoggingMenu(app));
		return retVal;
	}
	
	private static JMenu createVisualMenu(App app) {
		var retVal = new JMenu("Visual");
		retVal.add(new ShowSunDialogAction(app));
		return retVal;
	}
	
	private static JMenu createHelpMenu(App app) {
		var retVal = new JMenu("Help");
		retVal.add(createAboutItem(app));
		return retVal;
	}
	
	private static JMenuItem createAboutItem(App app) {
		var retVal = new JMenuItem("About...");
		retVal.addActionListener(e -> AboutDialog.show(app));
		return retVal;
	}
	
}
