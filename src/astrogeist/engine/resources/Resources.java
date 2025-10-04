package astrogeist.engine.resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;

import astrogeist.common.Guards;
import astrogeist.engine.util.Instants;

public final class Resources {
	private Resources() { Guards.throwStaticClassInstantiateError(); }
	
	private static boolean _dev = true;
	
	public static final String LOGO_PATH = "/astrogeist/engine/resources/logo.png";
	
	private static URL _logoUrl = null;
	
	public static void setDevelopmentMode(boolean v) { _dev = v; }
	
	public static boolean isDevelopmentMode() { return _dev; }
	
	public static URL getLogoUrl(Object o) { 
		if (_logoUrl != null) return _logoUrl;
		_logoUrl = o.getClass().getResource(LOGO_PATH);
		return _logoUrl;
	}
	
	private static File _astrogeistDir = null;
	private static File _settingsFile = null;
	private static File _userDataDefinitionsFile = null;
	private static File _userDataDir = null;
	private static File _regexScannerPatternFile = null;
	private static File _timelineMappingFile = null;
	private static File _scanningConfigFile = null;
	
	public static File ensureAstrogeistDirectoryExist() throws IOException {
		if (_astrogeistDir != null) return _astrogeistDir;
		
		var homeDir = new File(System.getProperty("user.home"));
		
		var astrogeistDir = _dev ? new File(homeDir, "Astrogeist.Dev") : new File(homeDir, "Astrogeist");
		
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
		_settingsFile = new File(_astrogeistDir, "settings.xml");
		return _settingsFile;
	}
	
	public static File getUserDataDefinitionsFile() {
		checkGotAstrogeistDir();
		if (_userDataDefinitionsFile != null) return _userDataDefinitionsFile;
		_userDataDefinitionsFile = new File(_astrogeistDir, "userdatadef.xml");
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
		_regexScannerPatternFile = new File(_astrogeistDir, "scan-regex.xml");
		return _regexScannerPatternFile;
	}
	
	public static File getScanningConfigFile() {
		checkGotAstrogeistDir();
		if (_scanningConfigFile != null) return _scanningConfigFile;
		_scanningConfigFile = new File(_astrogeistDir, "scanning.xml");
		return _scanningConfigFile;
	}
	
	public static File getTimelineMappingPatternFile() {
		checkGotAstrogeistDir();
		if (_timelineMappingFile != null) return _timelineMappingFile;
		_timelineMappingFile = new File(_astrogeistDir, "timeline.mapping.xml");
		return _timelineMappingFile;
	}
	
	private static void checkGotAstrogeistDir() {
		if (_astrogeistDir == null) throw new AssertionError("Missing astrogeist dir"); }
}
