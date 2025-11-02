package astrogeist.engine.persitence.disk;

import java.nio.file.Path;

import aha.common.appdata.DiskAppDataAccessor;
import astrogeist.engine.resources.Resources;

public final class DiskAstrogeistAccessor extends DiskAppDataAccessor {
	
	public DiskAstrogeistAccessor() { super(getFolder()); }
	
	private final static Path getFolder() {
		var root = Resources.getAstrogeistDirectoryAsPath();
		var folder = root.resolve("app-data");
		return folder;
	}
	
}
