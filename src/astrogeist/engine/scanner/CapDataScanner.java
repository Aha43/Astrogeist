package astrogeist.engine.scanner;

import java.nio.file.Path;
import java.util.logging.Logger;

import astrogeist.engine.abstraction.FileParser;
import astrogeist.engine.abstraction.PluginScanner;
import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.UtcExtractor;
import astrogeist.engine.logging.Log;
import astrogeist.engine.util.FilesUtil;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

/**
 * <p>
 *   Generic scanner that assumes UTC time for snapshots can be extracted from path.
 * </p>
 */
public abstract class CapDataScanner implements PluginScanner {
	private final Logger logger = Log.get(this);
	
	private final UtcExtractor utcExtractor = new DefaultUtcExtractor();
	
	private final CompositeFileParser fileParser;
	
	private final String location;
	
	protected CapDataScanner(String location, FileParser... fileparsers) {
		this.location = location;
		this.fileParser = new CompositeFileParser(fileparsers);
	}

	@Override public final void scan(Timeline timeline) throws Exception {
		try {
			var locPath = Path.of(this.location);
			var paths = FilesUtil.getRegularFilePaths(locPath);
			for (var path : paths) {
				
				this.logger.info("analyze path: " + path.toString());
				
				var instant = this.utcExtractor.extract(path);
				if (instant == null) continue;
				
				timeline.put(instant, path);
				
				this.fileParser.parse(instant, path.toFile(), timeline);
			}
		} catch (Exception x) {
			MessageDialogs.showError("Failed scanning", x);
		}
	}

}
