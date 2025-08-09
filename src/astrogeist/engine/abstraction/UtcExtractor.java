package astrogeist.engine.abstraction;

import java.nio.file.Path;
import java.time.Instant;

public interface UtcExtractor {
	Instant extract(Path path);
}
