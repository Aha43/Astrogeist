package aha.common.util;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

/**
 * <p>
 *   Utility methods of use when working with time stamps of files. 
 * </p>
 */
public final class FileTimes {
	private FileTimes() { throwStaticClassInstantiateError(); }
	
    /**
     * <p>
     *   Returns the creation time if supported by the OS/file system,
     *   otherwise falls back to last modified time.
     * </p>
     * @param path Path to file to get time for.
     */
    public final static Instant getCreationTime(Path path) throws IOException {
        var attrs = Files.readAttributes(path, BasicFileAttributes.class);

        var creation = attrs.creationTime();
        var modified = attrs.lastModifiedTime();

        // Some Linux file systems just return the same as lastModifiedTime
        if (creation != null && !creation.equals(modified)) 
        	return creation.toInstant();
        
        // Fallback: use last modified time
        return modified.toInstant();
    }

}
