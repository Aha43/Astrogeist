package astrogeist.engine.scanner.capdata;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import astrogeist.engine.abstraction.Scanner;
import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.UtcExtractor;
import astrogeist.engine.logging.Log;
import astrogeist.engine.scanner.AbstractScanner;
import astrogeist.engine.scanner.capdata.fileparsers.CompositeFileParser;
import astrogeist.engine.util.FilesUtil;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class CapDataScanner extends AbstractScanner {
	private final Logger logger = Log.get(this);
	
	private final UtcExtractor utcExtractor = new DefaultUtcExtractor();
	
	private final CompositeFileParser fileParser = new CompositeFileParser();
	
	protected CapDataScanner(File rootDir) { super(rootDir); }

	@Override public void scan(Timeline timeline) throws Exception {
		try {
			var paths = FilesUtil.getRegularFilePaths(super.rootDir.toPath());
			for (var path : paths) {
				
				this.logger.info("analyze path: " + path.toString());
				
				var instant = this.utcExtractor.extract(path);
				if (instant == null) continue;
				
				timeline.put(instant, path);
				
				fileParser.parse(instant, path.toFile(), timeline);
			}
		} catch (Exception x) {
			MessageDialogs.showError("Failed scanning", x);
		}
	}
	
	public static Scanner[] createScanners(){
		var retVal = new ArrayList<Scanner>();
		
		var roots = getRoots();
		for (var root : roots) retVal.add(new CapDataScanner(root));
		
		return retVal.toArray(Scanner.EmptyArray);
	}

}
