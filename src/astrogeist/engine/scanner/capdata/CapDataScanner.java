package astrogeist.engine.scanner.capdata;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.logging.Log;
import astrogeist.engine.scanner.AbstractScanner;
import astrogeist.engine.scanner.Scanner;
import astrogeist.engine.scanner.capdata.fileparsers.CompositeFileParser;

public class CapDataScanner extends AbstractScanner {
	private final Logger logger = Log.get(this);
	
	private final UtcExtractor utcExtractor = new DefaultUtcExtractor();
	
	private final CompositeFileParser fileParser = new CompositeFileParser();
	
	protected CapDataScanner(File rootDir) { super(rootDir); }

	@Override public void scan(Timeline timeline) throws Exception {
		var paths = getPaths(super.rootDir);
		for (var path : paths) {
			this.logger.info("analyze path: " + path.toString());
			var instant = this.utcExtractor.extract(path);
			if (instant == null) continue;
			
			this.logger.info("  extracted time: " + instant.toString());
			
			timeline.put(instant, path);
			
			//timeline.put(instant, fileName, new TimelineValue(path.toString(), Type.DiskFile().resolve(fileName)));
			//timeline.put(instant, fileName, path.toString(), Type.DiskFile().resolve(fileName));
			
			fileParser.parse(instant, path.toFile(), timeline);
		}
	}
	
	private static List<Path> getPaths(File dir) throws Exception {
		var retVal = new ArrayList<Path>();
		Files.walk(dir.toPath()).filter(Files::isRegularFile).forEach(retVal::add);
		return retVal;
	}
	
	public static Scanner[] createScanners(){
		var retVal = new ArrayList<Scanner>();
		
		var roots = getRoots();
		for (var root : roots) retVal.add(new CapDataScanner(root));
		
		return retVal.toArray(Scanner.EmptyArray);
	}

}
