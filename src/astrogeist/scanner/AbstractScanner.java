package astrogeist.scanner;

import java.io.File;
import java.util.List;

import astrogeist.app.dialog.message.MessageDialogs;
import astrogeist.setting.SettingKeys;
import astrogeist.setting.Settings;

public abstract class AbstractScanner implements Scanner {
	
	private final File rootDir;
	
	protected AbstractScanner(File rootDir) { this.rootDir = rootDir; }
	
	protected File getRootDir() { return this.rootDir; }
	
	protected static List<File> getRoots() {
		var roots = Settings.getPaths(SettingKeys.DATA_ROOTS);
		if (roots.size() == 0) {
			MessageDialogs.showWarning("No root directories been specified, do in settings");
		}
		return roots;
	}
}
