package astrogeist.engine.file;

import java.nio.file.Path;
import java.time.Instant;

import aha.common.util.FilesUtil;
import astrogeist.engine.typesystem.Type;

public record FileRecord(Type.DiskFile fileType, Path path, long sizeKB, 
	Instant timestamp, String lastModified) {
	
	public String name() { return FilesUtil.getBaseName(path); }
	public String extension() { return FilesUtil.getExtension(path); }
}
