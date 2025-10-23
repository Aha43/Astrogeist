package astrogeist.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

public final class FileTimes {
	private FileTimes() { Guards.throwStaticClassInstantiateError(); }
	
    /**
     * <p>
     *   Returns the creation time if supported by the OS/filesystem,
     *   otherwise falls back to last modified time.
     * </p>
     * @param path Path to file to get time for.
     */
    public static final Instant getCreationTime(Path path) throws IOException {
        var attrs = Files.readAttributes(path, BasicFileAttributes.class);

        var creation = attrs.creationTime();
        var modified = attrs.lastModifiedTime();

        // Some Linux filesystems just return the same as lastModifiedTime
        if (creation != null && !creation.equals(modified)) return creation.toInstant();
        
        // Fallback: use last modified time
        return modified.toInstant();
    }

}
