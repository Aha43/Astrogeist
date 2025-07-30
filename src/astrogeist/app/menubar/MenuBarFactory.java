package astrogeist.app.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import astrogeist.Common;
import astrogeist.app.App;
import astrogeist.app.dialog.AboutDialog;
import astrogeist.app.dialog.settings.SettingsDialog;

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
		settingsItem.addActionListener(e -> {
			SettingsDialog dialog = new SettingsDialog(app);
			dialog.setVisible(true);
		});
		
		return settingsItem;
	}
	
	private static JMenuItem createExitItem() {
		var exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(e -> System.exit(0));
		
		return exitItem;
	}
	
	private static JMenu createHelpMenu(App app) {
		var helpMenu = new JMenu("Help");
		
		helpMenu.add(createAboutItem(app));
		
		return helpMenu;
	}
	
	private static JMenuItem createAboutItem(App app) {
		var aboutItem = new JMenuItem("About...");
		aboutItem.addActionListener(e -> {
		    var dialog = new AboutDialog(app);
		    dialog.setVisible(true);
		});
		
		return aboutItem;
	}

	private MenuBarFactory() { Common.throwStaticClassInstantiateError(); }
}
