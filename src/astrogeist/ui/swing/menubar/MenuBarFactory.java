package astrogeist.ui.swing.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import astrogeist.common.Guards;
import astrogeist.engine.abstraction.persistence.AstrogeistStorageManager;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.about.AboutDialog;
import astrogeist.ui.swing.dialog.logging.LoggingMenu;
import astrogeist.ui.swing.dialog.settings.SettingsDialog;
import astrogeist.ui.swing.tool.sun.sketching.ShowSunDialogAction;

public final class MenuBarFactory {
	private MenuBarFactory() { Guards.throwStaticClassInstantiateError(); }
	
	public final static JMenuBar createMenuBar(App app, 
		AstrogeistStorageManager astrogeistStorageManager, TimelineNames timelineNames) {
		
		var menuBar = new JMenuBar();
		menuBar.add(createAstrogeistMenu(app, astrogeistStorageManager, timelineNames));
		menuBar.add(createDiagnosticMenu(app));
		menuBar.add(createVisualMenu(app));
		menuBar.add(createHelpMenu(app));
		return menuBar;
	}
	
	private final static JMenu createAstrogeistMenu(App app,
		AstrogeistStorageManager astrogeistStorageManager, TimelineNames timelineNames) {
		
		var retVal = new JMenu("File");
		retVal.add(createSettingsItem(app, astrogeistStorageManager, timelineNames));
		retVal.add(createExitItem());
		return retVal;
	}
	
	private final static JMenuItem createSettingsItem(App app,
		AstrogeistStorageManager astrogeistStorageManager, TimelineNames timelineNames) {
		
		var retVal = new JMenuItem("Settings");
		retVal.addActionListener(e -> SettingsDialog.show(app, astrogeistStorageManager, timelineNames));
		return retVal;
	}
	
	private final static JMenuItem createExitItem() { 
		var retVal = new JMenuItem("Exit");
		retVal.addActionListener(e -> System.exit(0));
		return retVal;
	}
	
	private final static JMenu createDiagnosticMenu(App app) {
		var retVal = new JMenu("Diagnostic");
		retVal.add(new LoggingMenu(app));
		return retVal;
	}
	
	private final static JMenu createVisualMenu(App app) {
		var retVal = new JMenu("Visual");
		retVal.add(new ShowSunDialogAction(app));
		return retVal;
	}
	
	private final static JMenu createHelpMenu(App app) {
		var retVal = new JMenu("Help");
		retVal.add(createAboutItem(app));
		return retVal;
	}
	
	private final static JMenuItem createAboutItem(App app) {
		var retVal = new JMenuItem("About...");
		retVal.addActionListener(e -> AboutDialog.show(app));
		return retVal;
	}
	
}
