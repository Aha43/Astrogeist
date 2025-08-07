package astrogeist.engine.file;

import java.nio.file.Path;
import java.time.Instant;

import astrogeist.engine.typesystem.Type;
import astrogeist.engine.util.FilesUtil;

public record FileRecord(Type.DiskFile fileType, Path path, long sizeKB, Instant timestamp, String lastModified) {
	
	public String getName() { return FilesUtil.getBaseName(path.toFile()); }
	public String getExtension() { return FilesUtil.getExtension(path); }
}
