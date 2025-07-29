package astrogeist.app;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import astrogeist.app.resources.Resources;
import astrogeist.setting.SettingsIO;

public final class Main {
	public static void main(String[] args) {
		try {
			initialize();
			var app = new App();
			SwingUtilities.invokeLater(() -> app.createGUI());
		} catch (Exception e) {
			e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Failed to load configuration.", "Error", JOptionPane.ERROR_MESSAGE);
	        System.exit(1);
		}
	}
	
	private static void initialize() throws Exception {
		Resources.ensureAstrogeistDirectoryExist();
		SettingsIO.loadOrCreate();
	}
	
}
