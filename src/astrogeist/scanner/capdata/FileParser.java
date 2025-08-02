package astrogeist.scanner.capdata;

import java.io.File;

import astrogeist.timeline.Timeline;

public interface FileParser {
	String getFileType();
	void parse(File file, Timeline timeline);
}
