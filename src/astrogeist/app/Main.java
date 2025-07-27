package astrogeist.app;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import astrogeist.setting.SettingsIO;

public final class Main {
	public static void main(String[] args) {
		try {
			SettingsIO.loadOrCreate();
			var app = new App();
			SwingUtilities.invokeLater(() -> app.createGUI());
		} catch (IOException e) {
			e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Failed to load configuration.", "Error", JOptionPane.ERROR_MESSAGE);
	        System.exit(1);
		}
	}
	
}
