package astrogeist.engine.scanner.capdata.fileparsers.sharpcap;

import java.io.File;
import java.time.Instant;

import astrogeist.engine.abstraction.FileParser;
import astrogeist.engine.abstraction.Timeline;

public class SharpCapFileParser implements FileParser {
	@Override public boolean canParse(File file) { 
		return file.getName().endsWith(".CameraSettings.txt"); }

	@Override public void parse(Instant time, File file, Timeline timeline) {
		var data = CameraSettingParser.parseFile(file);
		timeline.put(time, data);
	}

}
