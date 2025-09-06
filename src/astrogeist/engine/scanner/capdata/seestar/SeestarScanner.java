package astrogeist.engine.scanner.capdata.seestar;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
		var pathsDone = new HashSet<Path>();
		
		var dict = new LinkedHashMap<String, String>();
		
		var locPath = Path.of(super.location());
		var paths = FilesUtil.getRegularFilePaths(locPath);
		for (var path : paths) {
			
			this.logger.info("analyze path: " + path.toString());
			
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
