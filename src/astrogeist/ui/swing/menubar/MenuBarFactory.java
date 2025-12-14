package astrogeist.ui.swing.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import aha.common.abstraction.io.appdata.AppDataManager;
import aha.common.guard.ObjectGuards;
import aha.common.ui.swing.diagnostic.LoggingMenu;
import aha.common.ui.swing.panels.MemoryInspectorAction;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.about.AboutDialog;
import astrogeist.ui.swing.dialog.settings.SettingsDialog;
import astrogeist.ui.swing.integration.runconfig.RunConfigurationsMenu;
import astrogeist.ui.swing.tool.sun.sketching.ShowSunDialogAction;

/**
 * <p>
 *   Builds the menu hierarchy of Astrogeist's Swing GUI.
 * </p>
 */
public final class MenuBarFactory {
	private MenuBarFactory() { ObjectGuards.throwStaticClassInstantiateError(); }
	
	public final static JMenuBar createMenuBar(
		App app, 
		AppDataManager adm,
		TimelineNames timelineNames,
		SnapshotSelectionService sss) {
		
		var menuBar = new JMenuBar();
		menuBar.add(createAstrogeistMenu(app, adm, timelineNames));
		menuBar.add(createDiagnosticMenu(app));
		menuBar.add(createVisualMenu(app));
		menuBar.add(createRunMenu(adm, sss));
		menuBar.add(createHelpMenu(app));
		return menuBar;
	}
	
	private final static JMenu createAstrogeistMenu(App app, AppDataManager adm,
		TimelineNames timelineNames) {
		
		var retVal = new JMenu("File");
		retVal.add(createSettingsItem(app, adm, timelineNames));
		retVal.add(createExitItem());
		return retVal;
	}
	
	private final static JMenuItem createSettingsItem(App app, 
		AppDataManager adm, TimelineNames timelineNames) {
		
		var retVal = new JMenuItem("Settings");
		retVal.addActionListener(e -> SettingsDialog.show(app, adm,
			timelineNames));
		return retVal;
	}
	
	private final static JMenuItem createExitItem() { 
		var retVal = new JMenuItem("Exit");
		retVal.addActionListener(e -> System.exit(0));
		return retVal;
	}
	
	private final static JMenu createDiagnosticMenu(App app) {
		var retVal = new JMenu("Diagnostic");
		retVal.add(new LoggingMenu(app.getFrame()));
		retVal.add(new MemoryInspectorAction(app.getFrame()));
		return retVal;
	}
	
	private final static JMenu createVisualMenu(App app) {
		var retVal = new JMenu("Visual");
		retVal.add(new ShowSunDialogAction(app));
		return retVal;
	}
	
	private final static JMenu createRunMenu(AppDataManager adm,
		SnapshotSelectionService sss) {
		
		var retVal = new RunConfigurationsMenu(adm, sss);
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
