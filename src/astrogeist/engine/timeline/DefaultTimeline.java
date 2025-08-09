package astrogeist.engine.timeline;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;

import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.TimelineValuePool;
import astrogeist.engine.scanner.NormalizedProperties;

public final class DefaultTimeline implements Timeline {
	private final TimelineValuePool pool;
	
	private final NavigableMap<Instant, NavigableMap<String, TimelineValue>> timeline = new TreeMap<>();
	
	private static NavigableMap<String, TimelineValue> newSnapshotMap() {
	    return new TreeMap<>(Comparator.naturalOrder()); } // or a custom key order
	
	public DefaultTimeline(TimelineValuePool pool) { this.pool = pool; }
	
	@Override public void clear() { this.timeline.clear(); }
	
	@Override public void put(Instant time, Path file) {
		var fileTlv = this.pool.getFileValue(file);
		timeline.computeIfAbsent(time, t -> newSnapshotMap()).put(fileTlv.value(), fileTlv);
	}
	
	@Override public void put(Instant time, LinkedHashMap<String, String> values) {
	    var snap = timeline.computeIfAbsent(time, t -> newSnapshotMap());
	    for (var e : values.entrySet()) {
	        var nk = NormalizedProperties.getNormalized(e.getKey());
	        if (nk == null) continue;
	        snap.put(nk, this.pool.get(nk, e.getValue()));
	    }
	}
    
    @Override public void putTimelineValues(Instant time, LinkedHashMap<String, TimelineValue> values) {
    	for (var e : values.entrySet()) {
    		var key = e.getKey();
    		var tlv = e.getValue();
    		timeline.computeIfAbsent(time, t -> newSnapshotMap()).put(key, tlv);
    	}
    }
    
    @Override public Map<String, TimelineValue> snapshot(Instant time) {
        var m = timeline.get(time);
        return m == null ? Map.of() : Map.copyOf(m); // unmodifiable
    }
    
    @Override public NavigableSet<Instant> timestamps() { return timeline.navigableKeySet(); } // already chronological
}
