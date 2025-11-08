package astrogeist.engine.appdata;

import java.nio.file.Path;

import aha.common.abstraction.appdata.AppDataAccessor;
import aha.common.io.appdata.DiskAppDataAccessor;
import astrogeist.engine.resources.Resources;

/**
 * <p>
 *   {@link AppDataAccessor} used to read Astrogeist app data.
 * </p>
 */
public final class AstrogeistDiskAppDataAccessor extends DiskAppDataAccessor {
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 */
	public AstrogeistDiskAppDataAccessor() { super(getFolder()); }
	
	private final static Path getFolder() {
		var root = Resources.getAstrogeistDirectoryAsPath();
		var folder = root.resolve("app-data");
		return folder;
	}
	
}
