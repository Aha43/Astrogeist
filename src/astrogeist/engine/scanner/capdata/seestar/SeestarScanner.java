package astrogeist.engine.scanner.capdata.seestar;

import java.nio.file.Path;
import java.util.logging.Logger;

import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.logging.Log;
import astrogeist.engine.scanner.AbstractPluginScanner;
import astrogeist.engine.util.FileTimes;
import astrogeist.engine.util.FilesUtil;

public final class SeestarScanner extends AbstractPluginScanner {
	private final Logger logger = Log.get(this);
	
	public SeestarScanner(String location) { super(location); }
	
	@Override final public void scan(Timeline timeline) throws Exception {
		var locPath = Path.of(super.location());
		var paths = FilesUtil.getRegularFilePaths(locPath);
		for (var path : paths) {
			var time = FileTimes.getCreationTime(path);
			this.logger.info("analyze path: " + path.toString() + " - t: " + time);
			
		}
		
	}

}
