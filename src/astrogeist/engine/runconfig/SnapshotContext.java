package astrogeist.engine.runconfig;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

public final class SnapshotContext {
    private final Path rawDir;
    private final String target;
    private final Instant start;

    public SnapshotContext(Path rawDir, String target, Instant start) {
        this.rawDir = rawDir;
        this.target = target;
        this.start = start;
    }
    
    public final Path rawDir() { return this.rawDir; }
    public final String target() { return this.target; }
    public final Instant start() { return this.start; }
    
    public final static SnapshotContext of(String rawDir, String target,
    	String stampIso) {
    	
        Instant t = Instant.parse(stampIso);
        return new SnapshotContext(Paths.get(rawDir), target, t);
    }
}
