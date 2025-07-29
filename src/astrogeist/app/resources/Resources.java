package astrogeist.app.resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import astrogeist.Common;

public final class Resources {
	private static URL _logoUrl = null;
	public static URL getLogoUrl(Object o) { 
		if (_logoUrl != null) return _logoUrl;
		_logoUrl = o.getClass().getResource("/astrogeist/app/resources/logo.png");
		return _logoUrl;
	}
	
	public static File _astrogeistDir = null;
	public static File _settingsFile = null;
	
	public static File ensureAstrogeistDirectoryExist() throws IOException {
		if (_astrogeistDir != null) return _astrogeistDir;
		
		var homeDir = new File(System.getProperty("user.home"));
		var astrogeistDir = new File(homeDir, ".astrogeist");
		if (astrogeistDir.exists()) {
			_astrogeistDir = astrogeistDir;
			return _astrogeistDir;
		}
		if (astrogeistDir.mkdirs()) {
			_astrogeistDir = astrogeistDir;
			return _astrogeistDir;	
		}
		
		throw new IOException("Can not create dir: " + astrogeistDir.getAbsolutePath());
	}
	
	public static File getAstrogeistDirectory() { checkGotAstrogeistDir(); return _astrogeistDir; }
	
	public static File getSettingsFile() {
		checkGotAstrogeistDir();
		if (_settingsFile != null) return _settingsFile;
		_settingsFile = new File(_astrogeistDir, "astrogeist.settings.txt");
		return _settingsFile;
	}
	
	private static void checkGotAstrogeistDir() {
		if (_astrogeistDir == null) throw new AssertionError("Missing astrogeist dir");
	}

	private Resources() { Common.throwStaticClassInstantiateError(); }
}
