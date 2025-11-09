package aha.common.io.appdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import aha.common.abstraction.io.appdata.AppData;
import aha.common.abstraction.io.appdata.AppDataAccessor;
import aha.common.abstraction.io.appdata.AppDataReader;
import aha.common.abstraction.io.appdata.AppDataWriter;
import aha.common.logging.Log;

/**
 * <p>
 *   Base class for
 *   {@link AppDataAccessor} implementations that read from file system and
 *   save to file system.
 * </p>
 */
public abstract class DiskAppDataAccessor implements AppDataAccessor {
	private final Logger logger = Log.get(this);
	
	private final Path folder;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param folder Root folder for application data.
	 */
	protected DiskAppDataAccessor(Path folder) { this.folder = folder; }
	
	private final File getFile(AppData data) throws Exception {
		Files.createDirectories(this.folder);
		var name = data.type().getSimpleName();
		var format = data.format();
		var fileName = name + '.' + format;
		var retVal = this.folder.resolve(fileName).toFile();
		return retVal;
	}
	
	/**
	 * <p>
	 *   Gets root folder for application data.
	 * </p>
	 * @return the folder for application data.
	 */
	public final Path folder() { return this.folder; }

	@Override public final Object load(AppDataReader reader) throws Exception {
		var file = this.getFile(reader);
		
		this.logger.info("load from file : '" + file.getCanonicalPath() +
			"' using reader of type : '" + reader.getClass().getName() + "'");
		
		if (!file.exists()) {
			this.logger.info("file does not exists: returns default");
			return reader.createDefault();
		}
		
		try (var in = new FileInputStream(file)) {
			return reader.read(in); }
	}

	@Override public final void save(AppDataWriter writer, Object data) throws Exception {
		var file = this.getFile(writer);
		
		this.logger.info("save to file : '" + file.getCanonicalPath() +
			"' using writer of type : '" + writer.getClass().getName() + "'");
		
		try (var out = new FileOutputStream(file)) {
			writer.write(out, data); }
	}
	
}
