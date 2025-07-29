package astrogeist.app;

import java.io.IOException;

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
		} catch (IOException e) {
			e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Failed to load configuration.", "Error", JOptionPane.ERROR_MESSAGE);
	        System.exit(1);
		}
	}
	
	private static void initialize() throws IOException {
		Resources.ensureAstrogeistDirectoryExist();
		SettingsIO.loadOrCreate();
	}
	
}
