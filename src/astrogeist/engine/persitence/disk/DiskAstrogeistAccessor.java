package astrogeist.engine.persitence.disk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import astrogeist.engine.abstraction.persistence.AstrogeistAccessor;
import astrogeist.engine.abstraction.persistence.AstrogeistData;
import astrogeist.engine.abstraction.persistence.AstrogeistDataReader;
import astrogeist.engine.abstraction.persistence.AstrogeistDataWriter;
import astrogeist.engine.logging.Log;
import astrogeist.engine.resources.Resources;

public final class DiskAstrogeistAccessor implements AstrogeistAccessor {
	private final Logger logger = Log.get(this);
	
	private final Path folder;
	
	public DiskAstrogeistAccessor() {
		var root = Resources.getAstrogeistDirectoryAsPath();
		folder = root.resolve("app-data");
	}
	
	private final File getFile(AstrogeistData data) throws Exception {
		Files.createDirectories(this.folder);
		var name = data.type().getSimpleName();
		var format = data.format();
		var fileName = name + '.' + format;
		var retVal = this.folder.resolve(fileName).toFile();
		return retVal;
	}
	
	public final Path folder() { return this.folder; }

	@Override public final Object load(AstrogeistDataReader reader) throws Exception {
		var file = this.getFile(reader);
		
		this.logger.info("load from file : '" + file.getCanonicalPath() + "' using reader of type : '" + reader.getClass().getName() + "'");
		
		if (!file.exists()) {
			this.logger.info("file does not exists: returns default");
			return reader.createDefault();
		}
		
		try (var in = new FileInputStream(file)) {
			return reader.read(in); }
	}

	@Override public final void save(AstrogeistDataWriter writer, Object data) throws Exception {
		var file = this.getFile(writer);
		
		this.logger.info("save to file : '" + file.getCanonicalPath() + "' using writer of type : '" + writer.getClass().getName() + "'");
		
		try (var out = new FileOutputStream(file)) {
			writer.write(out, data); }
	}
	
}
