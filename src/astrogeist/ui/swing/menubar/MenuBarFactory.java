package astrogeist.ui.swing.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import astrogeist.Common;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.actions.ExitAction;
import astrogeist.ui.swing.dialog.about.AboutDialog;
import astrogeist.ui.swing.dialog.settings.SettingsDialog;

public final class MenuBarFactory {
	
	public static JMenuBar createMenuBar(App app) {
		var menuBar = new JMenuBar();
		menuBar.add(createFileMenu(app));
		menuBar.add(createHelpMenu(app));
		return menuBar;
	}
	
	private static JMenu createFileMenu(App app) {
		var fileMenu = new JMenu("File");
		fileMenu.add(createSettingsItem(app));
		fileMenu.add(createExitItem());
		return fileMenu;
	}
	
	private static JMenuItem createSettingsItem(App app) {
		var settingsItem = new JMenuItem("Settings");
		settingsItem.addActionListener(e -> SettingsDialog.show(app));
		return settingsItem;
	}
	
	private static JMenuItem createExitItem() { return new JMenuItem(ExitAction.INSTANCE); }
	
	private static JMenu createHelpMenu(App app) {
		var helpMenu = new JMenu("Help");
		helpMenu.add(createAboutItem(app));
		return helpMenu;
	}
	
	private static JMenuItem createAboutItem(App app) {
		var aboutItem = new JMenuItem("About...");
		aboutItem.addActionListener(e -> AboutDialog.show(app));
		return aboutItem;
	}

	private MenuBarFactory() { Common.throwStaticClassInstantiateError(); }
}
