package astrogeist.engine.persitence.disk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import astrogeist.common.Strings;
import astrogeist.engine.abstraction.persistence.AstrogeistDataHandler;
import astrogeist.engine.abstraction.persistence.AstrogeistDataReaderWriter;
import astrogeist.engine.resources.Resources;

public abstract class DiskAstrogeistDataHandler implements AstrogeistDataHandler {
	
	private final String key;
	
	private final Path folder;
	private final File file;
	
	public DiskAstrogeistDataHandler(String key) {
		this.key = Strings.ensureFileNameValid(key);
		
		var root = Resources.getAstrogeistDirectoryAsPath();
		folder = root.resolve("app-data");
		file = root.resolve(key).toFile();
	}
	
	public final Path folder() { return this.folder; }
	
	public final File file() { return this.file; }

	@Override public final String key() { return this.key; }

	@Override public final Object load(AstrogeistDataReaderWriter reader) throws Exception {
		Files.createDirectories(this.folder);
		if (!this.file.exists()) return createDefault();
		try (var in = new FileInputStream(file)) {
			return reader.read(in); }
	}

	@Override public final void save(AstrogeistDataReaderWriter writer, Object data) throws Exception {
		try (var out = new FileOutputStream(this.file)) {
			writer.write(out, data); }
	}
	
	protected abstract <T> T createDefault();

}
