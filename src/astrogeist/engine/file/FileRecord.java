package astrogeist.engine.file;

import java.nio.file.Path;
import java.time.Instant;

import astrogeist.common.FilesUtil;
import astrogeist.engine.typesystem.Type;

public record FileRecord(Type.DiskFile fileType, Path path, long sizeKB, Instant timestamp, String lastModified) {
	public String getName() { return FilesUtil.getBaseName(path); }
	public String getExtension() { return FilesUtil.getExtension(path); }
}
