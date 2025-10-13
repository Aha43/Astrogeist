package astrogeist.engine.timeline;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Logger;

import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.logging.Log;

/**
 * <p>
 *   Implementation of the non filtered time line.
 * </p>
 */
public final class DefaultTimeline implements Timeline {
	private final Logger logger = Log.get(this);
	
	private final TimelineNames timelineNames;
	
	private final TimelineValuePool pool;

    // Chronological, thread-safe, navigable
    private final ConcurrentNavigableMap<Instant, ConcurrentNavigableMap<String, TimelineValue>> timeline =
    		new ConcurrentSkipListMap<>();

    private static ConcurrentNavigableMap<String, TimelineValue> newSnapshotMap() {
        // Sorted by key (natural order). If you need case-insensitive: String.CASE_INSENSITIVE_ORDER
        return new ConcurrentSkipListMap<>();
    }

    public DefaultTimeline(
    	TimelineValuePool pool,
    	TimelineNames timelineNames) {
    	this.timelineNames = timelineNames;
    	this.pool = pool; 
    }

    @Override public final void clear() {
    	this.logger.info("clears timeline");
    	
    	this.timeline.clear();
    }

    @Override public void put(Instant time, Path file) {
    	this.logger.info("put at time : '" + time + "' file : '" + file + "'");
    	
        var fileTlv = this.pool.getFileValue(file);
        timeline.computeIfAbsent(time, t -> newSnapshotMap())
        	.put(fileTlv.value(), fileTlv); // key = canonical full path (from pool)
    }
    
    @Override public final void put(Instant time, String name, String value) {
    	this.logger.info("put at time : '" + time + "' : '" + name + "' = '" + value + "'");
    	
    	var nk = this.timelineNames.getTimelineName(name);
    	if (nk == null) return;
    	var snap = this.timeline.computeIfAbsent(time, t -> newSnapshotMap());
    	snap.put(nk, pool.get(nk, value));
    }

    @Override public final void put(Instant time, LinkedHashMap<String, String> values) {
        var snap = this.timeline.computeIfAbsent(time, t -> newSnapshotMap());
        for (var e : values.entrySet()) {
            var nk = this.timelineNames.getTimelineName(e.getKey());
            if (nk == null) continue;
            snap.put(nk, pool.get(nk, e.getValue()));
        }
    }

    @Override public final void putTimelineValues(Instant time, LinkedHashMap<String, TimelineValue> values) {
        var snap = this.timeline.computeIfAbsent(time, t -> newSnapshotMap());
        for (var e : values.entrySet()) {
            var nk = this.timelineNames.getTimelineName(e.getKey()); // keep normalization consistent
            if (nk == null) continue;
            snap.put(nk, e.getValue());
        }
    }

    @Override public final Map<String, TimelineValue> snapshot(Instant time) {
        var m = this.timeline.get(time);
        // Return a stable, unmodifiable snapshot view (copy = iteration-safe for UI)
        return m == null ? Map.of() : Map.copyOf(m);
    }

    @Override public final NavigableSet<Instant> timestamps() {
        // Live view is mutable; wrap to avoid external mutation, remains navigable/ordered
        return Collections.unmodifiableNavigableSet(this.timeline.navigableKeySet());
    }

    @Override public final void update(Instant t, Map<String, TimelineValue> values) {
        if (t == null || values == null || values.isEmpty()) return;
        var snap = this.timeline.get(t);
        if (snap == null) return;

        for (var e : values.entrySet()) {
            var nk = this.timelineNames.getTimelineName(e.getKey());
            if (nk == null) continue;

            var tlv = e.getValue();
            var v = (tlv == null ? null : tlv.value());

            // Treat null/blank/"-" as delete
            if (v == null || v.isBlank() || "-".equals(v)) {
                snap.remove(nk);
            } else {
                snap.put(nk, tlv);
            }
        }
    }

    @Override public final void updateStrings(Instant t, Map<String, String> values) {
        if (t == null || values == null || values.isEmpty()) return;
        var snap = this.timeline.get(t);
        if (snap == null) return;

        for (var e : values.entrySet()) {
            var nk = this.timelineNames.getTimelineName(e.getKey());
            if (nk == null) continue;

            var raw = e.getValue();
            if (raw == null || raw.isBlank() || "-".equals(raw)) {
                snap.remove(nk);
            } else {
                snap.put(nk, pool.get(nk, raw));  // canonicalize via pool
            }
        }
    }

    @Override public final void upsert(Instant t, String key, TimelineValue value) {
        if (t == null) return;
        var nk = this.timelineNames.getTimelineName(key);
        if (nk == null) return;

        var snap = this.timeline.get(t);
        if (snap == null) return;

        var v = (value == null ? null : value.value());
        if (v == null || v.isBlank() || "-".equals(v)) {
            snap.remove(nk);
        } else {
            snap.put(nk, value);
        }
    }

    @Override public final void remove(Instant t, String key) {
    	this.logger.info("remove key : '" + key +"' at time : '" + t + "'");
    	
        if (t == null) return;
        var nk = this.timelineNames.getTimelineName(key);
        if (nk == null) return;

        var snap = timeline.get(t);
        if (snap != null) snap.remove(nk);
    }

}
