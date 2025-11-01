package astrogeist.ui.swing;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarculaLaf;

import aha.common.Guards;
import astrogeist.engine.resources.Resources;
import astrogeist.ui.swing.dialog.launch.LaunchDialog;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class Main {
	public static void main(String[] args) {
		try {
	        UIManager.setLookAndFeel(new FlatDarculaLaf());
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
		
		
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
		//SettingsIo.loadOrCreate();
	}
	
	private Main() { Guards.throwStaticClassInstantiateError(); }
}
