package astrogeist.engine.scanner;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import astrogeist.engine.abstraction.FileParser;
import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.logging.Log;

/**
 * <p>
 *   {@link FileParser} that parses using given parsers.
 * </p>
 * <p>
 *   When this receives a file to parse it checks if it have a 
 *   {@link FileParser} that can parse it, if so, uses that. If
 *   none are found for the given file is ignored.
 * </p>
 */
public final class CompositeFileParser implements FileParser {
	private final Logger logger = Log.get(this);
	
	private final List<FileParser> parsers = new ArrayList<FileParser>();
	
	/**
	 * <p>
	 *   Constructor.
	 * </p> 
	 * @param fileParsers {@link FileParser}s this will use to parse.
	 */
	public CompositeFileParser(FileParser... fileParsers) { 
		for (var p : fileParsers) {
			this.parsers.add(p);
		} 
	}
	
	@Override public final boolean canParse(File file) { return findParser(file) != null; }
	
	@Override public final void parse(Instant time, File file, Timeline timeline) {
		var parser = findParser(file);
		if (parser != null) {
			this.logger.info("Parse file : '" + file + "' using file parse : '" + parser.getClass().getSimpleName() + "'");
			parser.parse(time, file, timeline);
			return;
		}
		this.logger.info("Found no parser to parse file : '" + file + "'");
	}
	
	private final FileParser findParser(File file) {
		for (var p : this.parsers) if (p.canParse(file)) return p;
		return null;
	}

}
