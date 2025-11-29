package astrogeist.ui.swing.integration.runconfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public final class FileCollection {
	
	private final Set<File> files = new LinkedHashSet<>();
	
	private final String type;
	
	public FileCollection(String type) { this.type = type; }
	
	public String type() { return this.type; }
	
	public final FileCollection add(File file) {
		requireNonNull(file, "file");
		
		
		
		this.files.add(file);
		return this;
	}

}
