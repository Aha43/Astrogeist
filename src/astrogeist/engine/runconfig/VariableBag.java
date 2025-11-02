package astrogeist.engine.runconfig;

import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import aha.common.OS;

public final class VariableBag extends HashMap<String,String> {
	private static final long serialVersionUID = 1L;
    
	static VariableBag from(SnapshotContext s, String workBase, 
		Map<String,String> env) {
        
		VariableBag v = new VariableBag();
        
        // General
        v.put("WORK_BASE", Paths.get(workBase).toAbsolutePath().toString());
        v.put("OS", OS.osName());
        v.put("AG_VERSION", "dev");
        // Snapshot
        v.put("SNAPSHOT.RAW_DIR", s.rawDir().toAbsolutePath().toString());
        v.put("TARGET_NAME", s.target());
        v.put("TARGET_SLUG", slug(s.target()));
        v.put("STAMP", DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        	.withZone(ZoneOffset.UTC).format(s.start()));
        // Env passthrough (ENV.X)
        env.forEach((k, val) -> v.put("ENV." + k, val));
        return v;
    }
    
	private final static String slug(String s) {
        return s == null ? "unknown"
        	: s.toLowerCase().replaceAll("[^a-z0-9]+", "-").
        		replaceAll("^[-]+|[-]+$", "");
    }
    
}
