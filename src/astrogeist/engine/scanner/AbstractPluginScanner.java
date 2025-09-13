package astrogeist.engine.scanner;

import astrogeist.engine.abstraction.PluginScanner;

public abstract class AbstractPluginScanner implements PluginScanner {
	private final String location;
	
	protected AbstractPluginScanner(String location) { this.location = location; }
	
	@Override public final String location() { return this.location; }
	
	@Override public String name() { return this.getClass().getSimpleName(); }
	
	@Override public String description() { return "Scanning : '" + this.location + "'"; }
}
