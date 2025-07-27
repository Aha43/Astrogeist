package astrogeist.app.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import astrogeist.app.dialog.AboutDialog;
import astrogeist.app.dialog.settings.SettingsDialog;

public final class MenuBarFactory {
	
	public static JMenuBar createMenuBar() {
		var menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createHelpMenu());
		return menuBar;
	}
	
	private static JMenu createFileMenu() {
		var fileMenu = new JMenu("File");
		
		fileMenu.add(createSettingsItem());
		fileMenu.add(createExitItem());
		
		return fileMenu;
	}
	
	private static JMenuItem createSettingsItem() {
		var settingsItem = new JMenuItem("Settings");
		settingsItem.addActionListener(e -> {
			SettingsDialog dialog = new SettingsDialog(null);
			dialog.setVisible(true);
		});
		
		return settingsItem;
	}
	
	private static JMenuItem createExitItem() {
		var exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(e -> System.exit(0));
		
		return exitItem;
	}
	
	private static JMenu createHelpMenu() {
		var helpMenu = new JMenu("Help");
		
		helpMenu.add(createAboutItem());
		
		return helpMenu;
	}
	
	private static JMenuItem createAboutItem() {
		var aboutItem = new JMenuItem("About...");
		aboutItem.addActionListener(e -> {
		    var dialog = new AboutDialog(null); // Replace with your main window
		    dialog.setVisible(true);
		});
		
		return aboutItem;
	}

	private MenuBarFactory() { throw new AssertionError("Can not instantiate static class"); }
}
