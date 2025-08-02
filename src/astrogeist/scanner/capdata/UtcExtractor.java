package astrogeist.scanner.capdata;

import java.nio.file.Path;
import java.time.Instant;

public interface UtcExtractor {
	Instant extract(Path path);
}
