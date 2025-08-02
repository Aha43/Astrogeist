package astrogeist.resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;

import astrogeist.Common;
import astrogeist.util.Instants;

public final class Resources {
	private static URL _logoUrl = null;
	public static URL getLogoUrl(Object o) { 
		if (_logoUrl != null) return _logoUrl;
		_logoUrl = o.getClass().getResource("/astrogeist/resources/logo.png");
		return _logoUrl;
	}
	
	private static File _astrogeistDir = null;
	private static File _settingsFile = null;
	private static File _userDataDefinitionsFile = null;
	private static File _userDataDir = null;
	private static File _regexScannerPatternFile = null;
	
	public static File ensureAstrogeistDirectoryExist(String path) throws IOException {
		if (_astrogeistDir != null) return _astrogeistDir;
		
		var homeDir = path != null ? new File(path) : new File(System.getProperty("user.home"));
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
		_settingsFile = new File(_astrogeistDir, "astrogeist.settings.xml");
		return _settingsFile;
	}
	
	public static File getUserDataDefinitionsFile() {
		checkGotAstrogeistDir();
		if (_userDataDefinitionsFile != null) return _userDataDefinitionsFile;
		_userDataDefinitionsFile = new File(_astrogeistDir, "astrogeist.userdatadef.xml");
		return _userDataDefinitionsFile;
	}
	
	public static File getUserDataDir() {
		checkGotAstrogeistDir();
		if (_userDataDir != null) return _userDataDir;
		var dir = new File(_astrogeistDir, "userdata");
		dir.mkdirs();
		_userDataDir = dir;
		return _userDataDir;
	}
	
	public static File getUserDataFile(Instant instant) {
		var dir = getUserDataDir();
		var fname = Instants.toFileSafeString(instant);
		return new File(dir, fname + ".xml");
	}
	
	public static File getRegexScannerPatternFile() {
		checkGotAstrogeistDir();
		if (_regexScannerPatternFile != null) return _regexScannerPatternFile;
		_regexScannerPatternFile = new File(_astrogeistDir, "astrogeist.scan-regex.xml");
		return _regexScannerPatternFile;
	}
	
	private static void checkGotAstrogeistDir() {
		if (_astrogeistDir == null) throw new AssertionError("Missing astrogeist dir");
	}

	private Resources() { Common.throwStaticClassInstantiateError(); }
}
