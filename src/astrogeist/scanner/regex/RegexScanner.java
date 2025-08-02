package astrogeist.scanner.regex;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import astrogeist.logging.Log;
import astrogeist.resources.Resources;
import astrogeist.scanner.AbstractScanner;
import astrogeist.scanner.Scanner;
import astrogeist.store.ObservationStore;

public class RegexScanner extends AbstractScanner {
	private final Logger logger = Log.get(this);
	
	protected RegexScanner(File rootDir) { super(rootDir); }

	@Override
	public void scan(ObservationStore store) throws Exception {
		var xmlFile = Resources.getRegexScannerPatternFile();
		var scanConfig = ScanConfigParser.parse(xmlFile);
		
		var timestampRegexResolvers = new ArrayList<TimestampRegexResolver>();
		for (var c : scanConfig.timestampResolvers) {
			var resolver = new TimestampRegexResolver(c.regex,	 c.timezone);
			timestampRegexResolvers.add(resolver);
		}
		
		var paths = getFiles(super.getRootDir());
		for (var path : paths) {
			this.logger.info("analyze: " + path.toString());
			for (var r : timestampRegexResolvers) {
				var time = r.extract(path);
				if (time.isPresent()) {
					this.logger.info("  extracted time: " + time.get().toString());
				}
			}
		}
	}
	
	private static List<Path> getFiles(File dir) throws Exception {
		var retVal = new ArrayList<Path>();
		Files.walk(dir.toPath()).filter(Files::isRegularFile).forEach(retVal::add);
		return retVal;
	}
	
	public static Scanner[] createScanners(){
		var retVal = new ArrayList<Scanner>();
		
		var roots = getRoots();
		for (var root : roots) retVal.add(new RegexScanner(root));
		
		return retVal.toArray(Scanner.EmptyArray);
	}

}
