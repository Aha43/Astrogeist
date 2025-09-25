package astrogeist.engine.scanner.sharpcap;

import java.io.File;
import java.time.Instant;

import astrogeist.engine.abstraction.FileParser;
import astrogeist.engine.abstraction.timeline.Timeline;

public final class SharpCapFileParser implements FileParser {
	@Override public final boolean canParse(File file) { 
		return file.getName().endsWith(".CameraSettings.txt"); 
	}

	@Override public final void parse(Instant time, File file, Timeline timeline) {
		var data = CameraSettingParser.parseFile(file);
		timeline.put(time, data);
	}

}
