package astrogeist.scanner.capdata;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import astrogeist.logging.Log;
import astrogeist.scanner.AbstractScanner;
import astrogeist.scanner.Scanner;
import astrogeist.timeline.Timeline;
import astrogeist.util.FilesUtil;

public class CapDataScanner extends AbstractScanner {
	private final Logger logger = Log.get(this);
	
	private final UtcExtractor utcExtractor = new DefaultUtcExtractor();
	
	protected CapDataScanner(File rootDir) { super(rootDir); }

	@Override
	public void scan(Timeline timeline) throws Exception {
		var paths = getPaths(super.getRootDir());
		for (var path : paths) {
			this.logger.info("analyze path: " + path.toString());
			var instant = this.utcExtractor.extract(path);
			if (instant == null) continue;
			
			this.logger.info("  extracted time: " + instant.toString());
			
			var extension = FilesUtil.getExtension(path);
			
			timeline.put(instant, extension, path.toString(), "file");
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
