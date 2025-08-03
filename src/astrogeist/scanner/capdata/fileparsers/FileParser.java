package astrogeist.scanner.capdata.fileparsers;

import java.io.File;
import java.time.Instant;

import astrogeist.timeline.Timeline;

public interface FileParser {
	boolean canParse(File file);
	void parse(Instant time, File file, Timeline timeline);
}
