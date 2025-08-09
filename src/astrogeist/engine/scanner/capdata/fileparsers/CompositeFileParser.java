package astrogeist.engine.scanner.capdata.fileparsers;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import astrogeist.engine.abstraction.FileParser;
import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.scanner.capdata.fileparsers.sharpcap.SharpCapFileParser;

public final class CompositeFileParser implements FileParser {
	private List<FileParser> parsers = new ArrayList<FileParser>();
	
	public CompositeFileParser() {
		parsers.add(new SharpCapFileParser()); 
	}
	
	@Override
	public boolean canParse(File file) { return findParser(file) != null; }
	
	@Override
	public void parse(Instant time, File file, Timeline timeline) {
		var parser = findParser(file);
		if (parser != null) parser.parse(time, file, timeline);
	}
	
	private FileParser findParser(File file) {
		for (var p : this.parsers) if (p.canParse(file)) return p;
		return null;
	}

}
