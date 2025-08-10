package astrogeist.ui.swing;

import javax.swing.SwingUtilities;

import astrogeist.Common;
import astrogeist.engine.resources.Resources;
import astrogeist.engine.setting.SettingsIo;
import astrogeist.ui.swing.dialog.launch.LaunchDialog;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class Main {
	public static void main(String[] args) {
		try {
			var launch = LaunchDialog.showStartupDialog();
			if (!launch.proceed()) System.exit(0);
			
			Resources.setDevelopmentMode(launch.developmentMode());
			
			initialize();
			var app = new App();
			SwingUtilities.invokeLater(() -> app.createGUI());
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialogs.showError("Failed to launch Astrogeist", e);
	        System.exit(1);
		}
	}
	
	private static void initialize() throws Exception {
		Resources.ensureAstrogeistDirectoryExist();
		SettingsIo.loadOrCreate();
	}
	
	private Main() { Common.throwStaticClassInstantiateError(); }
}
