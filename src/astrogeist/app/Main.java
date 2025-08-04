package astrogeist.app;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import astrogeist.resources.Resources;
import astrogeist.setting.SettingsIo;

public final class Main {
	public static void main(String[] args) {
		try {
			initialize(args);
			var app = new App();
			SwingUtilities.invokeLater(() -> app.createGUI());
		} catch (Exception e) {
			e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Failed to load configuration.", "Error", JOptionPane.ERROR_MESSAGE);
	        System.exit(1);
		}
	}
	
	private static void initialize(String[] arg) throws Exception {
		String path = arg.length > 0 ? arg[0] : null;
		Resources.ensureAstrogeistDirectoryExist(path);
		SettingsIo.loadOrCreate();
	}	
}
