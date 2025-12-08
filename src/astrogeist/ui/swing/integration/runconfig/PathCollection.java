package astrogeist.ui.swing.integration.runconfig;

import static aha.common.guard.PathGuards.require;

import java.nio.file.Path;
import java.util.LinkedHashSet;

public final class PathCollection {
	private final LinkedHashSet<Path> paths = new LinkedHashSet<>();
	
	private final String type;
	
	public PathCollection(String type) { this.type = type; }
	
	public final String type() { return this.type; }
	
	public final int size() { return this.paths.size(); }
	
	public final PathCollection add(Path path) {
		path = require(path).isFile().hasExtension(this.type).path();
		this.paths.add(path);
		return this;
	}
	
}
