package astrogeist.engine.scanner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Logger;

import aha.common.Guards;
import astrogeist.engine.abstraction.Scanner;
import astrogeist.engine.logging.Log;

public abstract class AbstractScanner implements Scanner {
	protected final Path path;
	
	protected final Logger logger = Log.get(this); 
	
	protected AbstractScanner(File path) {  
		Objects.requireNonNull(path, "path");
		this.path = path.toPath();
	}
	
	protected AbstractScanner(String path) {  
		Guards.requireNonEmpty(path, "path");
		this.path = Path.of(path);
	}
	
	@Override public boolean canScan() { return Files.exists(path); }
	@Override public String name() { return this.getClass().getSimpleName(); }	
	@Override public String description() { return "Scanning : '" + this.path + "'"; }
}
