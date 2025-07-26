package astrogeist.app.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import astrogeist.app.component.ObservationTablePanel;
import astrogeist.app.dialog.AboutDialog;
import astrogeist.app.dialog.settings.SettingsDialog;
import astrogeist.scanner.CompositeScanner;
import astrogeist.store.ObservationStore;

public final class MenuBarFactory {
	
	public static JMenuBar createMenuBar(ObservationTablePanel tablePanel) {
		var menuBar = new JMenuBar();
		menuBar.add(createFileMenu(tablePanel));
		menuBar.add(createHelpMenu());
		return menuBar;
	}
	
	private static JMenu createFileMenu(ObservationTablePanel tablePanel) {
		var fileMenu = new JMenu("File");
		var loadItem = new JMenuItem("Load");
		fileMenu.add(loadItem);
		loadItem.addActionListener(e -> {
			
			// Needs to be refactored...
			var scanner = new CompositeScanner();
			var store = new ObservationStore();
			scanner.scan(store);
			
			tablePanel.setStore(store);
		});
		
		var settingsItem = new JMenuItem("Settings");
		fileMenu.add(settingsItem);
		settingsItem.addActionListener(e -> {
			SettingsDialog dialog = new SettingsDialog(null);
			dialog.setVisible(true);
		});
		
		var exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(e -> System.exit(0));
		fileMenu.add(exitItem);
		
		return fileMenu;
	}
	
	private static JMenu createHelpMenu() {
		var helpMenu = new JMenu("Help");
		
		var aboutItem = new JMenuItem("About...");
		aboutItem.addActionListener(e -> {
		    var dialog = new AboutDialog(null); // Replace with your main window
		    dialog.setVisible(true);
		});
		helpMenu.add(aboutItem);
		
		return helpMenu;
	}

	private MenuBarFactory() { throw new AssertionError("Can not instanciate static class"); }
}
