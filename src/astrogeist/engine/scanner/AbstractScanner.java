package astrogeist.engine.scanner;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import aha.common.logging.Log;
import astrogeist.engine.abstraction.Scanner;

public abstract class AbstractScanner implements Scanner {
	protected final Path path;
	
	protected final Logger logger = Log.get(this); 
	
	protected AbstractScanner(File path) {  
		this.path = requireNonNull(path, "path").toPath(); }
	
	protected AbstractScanner(String path) {  
		this.path = Path.of(requireNonEmpty(path, "path")); }
	
	@Override public final boolean canScan() { return Files.exists(path); }
	
	@Override public String name() { 
		return this.getClass().getSimpleName(); }	
	
	@Override public String description() { 
		return "Scanning : '" + this.path + "'"; }
}
