package astrogeist.engine.abstraction;

import java.io.File;
import java.time.Instant;

public interface FileParser {
	boolean canParse(File file);
	void parse(Instant time, File file, Timeline timeline);
}
