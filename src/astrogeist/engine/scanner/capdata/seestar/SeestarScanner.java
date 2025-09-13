package astrogeist.engine.scanner.capdata.seestar;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.jobs.JobProgressListener;
import astrogeist.engine.async.CancellationToken;
import astrogeist.engine.logging.Log;
import astrogeist.engine.scanner.AbstractPluginScanner;
import astrogeist.engine.util.FileTimes;
import astrogeist.engine.util.FilesUtil;

public final class SeestarScanner extends AbstractPluginScanner {
	private final Logger logger = Log.get(this);
	
	public SeestarScanner(String location) { super(location); }
	
	@Override final public void run(
		Timeline input, 
		JobProgressListener listener, 
		CancellationToken token) throws Exception {
		
		var timeline = input;
		
		var pathsDone = new HashSet<Path>();
		
		var dict = new LinkedHashMap<String, String>();
		
		var locPath = Path.of(super.location());
		var paths = FilesUtil.getRegularFilePaths(locPath);
		
		listener.onStart(paths.size());
		
		for (var path : paths) {
			if (token.isCancelled()) break;
			
			this.logger.info("analyze path: " + path.toString());
			
			try {
				var parent = path.getParent();
				if (parent == null) continue;
				
				var time = FileTimes.getCreationTime(parent);
				
				timeline.put(time, path);
				
				if (!pathsDone.contains(parent)) {
					var parentComponent = parent.getFileName();
					if (parentComponent != null) {
						var name = parentComponent.toString();
						if (name != null && !name.isBlank()) {
							addInfoFromParent(name, time, timeline, dict);
						}
					}
					pathsDone.add(parent);
				}
				
				listener.onSuccess(path, "analyzed");
			}
			catch (Exception x) {
				this.logger.log(Level.WARNING, "failed to analyze path : '" + path + "'", x);
				listener.onFailure(path, x);
			}
		}
	}
	
	private final static void addInfoFromParent(String name, Instant time, Timeline timeline, LinkedHashMap<String, String> dictToUse) {
		dictToUse.clear();
		dictToUse.put("Telescope", "Seestar 50");
		
		var tokens = Arrays.stream(name.split("_"))
			.filter(s -> !s.isEmpty()).toArray(String[]::new);
		
		for (var i = 0; i < tokens.length; i++) {
			switch (i) {
				case 0 : 
					dictToUse.put("Subject", tokens[0]); break;
				case 1 :
					dictToUse.put("CaptureType", tokens[1]); break;
			}
		}
		
		timeline.put(time, dictToUse);
	}

}
