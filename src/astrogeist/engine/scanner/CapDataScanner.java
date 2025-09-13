package astrogeist.engine.scanner;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import astrogeist.engine.abstraction.FileParser;
import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.UtcExtractor;
import astrogeist.engine.abstraction.jobs.JobProgressListener;
import astrogeist.engine.async.CancellationToken;
import astrogeist.engine.logging.Log;
import astrogeist.engine.util.FilesUtil;

/**
 * <p>
 *   Generic scanner that assumes UTC time for snapshots can be extracted from path.
 * </p>
 */
public abstract class CapDataScanner extends AbstractPluginScanner {
	@Override public void run(
		Timeline input,
		JobProgressListener listener, 
		CancellationToken token) throws Exception {
		
		var timeline = input;
		
		var locPath = Path.of(super.location());
		var paths = FilesUtil.getRegularFilePaths(locPath);
		
		listener.onStart(paths.size());
		
		for (var path : paths) {
			if (token.isCancelled()) break;
			
			this.logger.info("analyze path: " + path.toString());
			
			try {
				var instant = this.utcExtractor.extract(path);
				if (instant == null) continue;
				
				timeline.put(instant, path);
				
				this.fileParser.parse(instant, path.toFile(), timeline);
			
				listener.onSuccess(path, "analyzed");
			} catch (Exception x) {
				this.logger.log(Level.WARNING, "failed to analyze path : '" + path + "'", x);
				listener.onFailure(path, x);
			}
		}		
	}

	private final Logger logger = Log.get(this);
	
	private final UtcExtractor utcExtractor = new DefaultUtcExtractor();
	
	private final CompositeFileParser fileParser;
	
	protected CapDataScanner(String location, FileParser... fileparsers) {
		super(location);
		this.fileParser = new CompositeFileParser(fileparsers);
	}

}
