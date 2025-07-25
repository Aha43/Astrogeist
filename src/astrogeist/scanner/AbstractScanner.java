package astrogeist.scanner;

import java.io.File;

public abstract class AbstractScanner implements Scanner {
	
	private final File _rootDir;
	
	protected AbstractScanner(File rootDir) { _rootDir = rootDir; }
	
	protected File getRootDir() { return _rootDir; }
}
