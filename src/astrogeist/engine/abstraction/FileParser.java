package astrogeist.engine.abstraction;

import java.io.File;
import java.time.Instant;

/**
 * <p>
 *   A component that extracts capture/observation metadata from files
 *   and adds it to a
 *   {@link Timeline}.
 * </p>
 */
public interface FileParser {
	/**
	 * <p>
	 *   Tells if can parse given file.
	 * </p>
	 * @param file {@link File} to check.
	 * @return {@code true} if this parser can handle the file;
	 *         {@code false} otherwise.
	 */
	boolean canParse(File file);
	
	/**
	 * <p>
	 *   Parses the specified file and augments the given
	 *   {@link Timeline} with any metadata extracted from it.
	 * </p>
	 * @param time     time of capture/observation.
	 * @param file     {@link File} to parse.
	 * @param timeline {@link Timeline} to which parsed data is added.
	 */
	void parse(Instant time, File file, Timeline timeline);
}
