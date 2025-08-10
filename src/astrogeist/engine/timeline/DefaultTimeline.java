package astrogeist.engine.timeline;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.TimelineValuePool;
import astrogeist.engine.scanner.NormalizedProperties;

public final class DefaultTimeline implements Timeline {
	private final TimelineValuePool pool;

    // Chronological, thread-safe, navigable
    private final ConcurrentNavigableMap<Instant, ConcurrentNavigableMap<String, TimelineValue>> timeline =
            new ConcurrentSkipListMap<>();

    private static ConcurrentNavigableMap<String, TimelineValue> newSnapshotMap() {
        // Sorted by key (natural order). If you need case-insensitive: String.CASE_INSENSITIVE_ORDER
        return new ConcurrentSkipListMap<>();
    }

    public DefaultTimeline(TimelineValuePool pool) { this.pool = pool; }

    @Override public void clear() { timeline.clear(); }

    @Override public void put(Instant time, Path file) {
        var fileTlv = pool.getFileValue(file);
        timeline.computeIfAbsent(time, t -> newSnapshotMap())
                .put(fileTlv.value(), fileTlv); // key = canonical full path (from pool)
    }

    @Override public void put(Instant time, LinkedHashMap<String, String> values) {
        var snap = timeline.computeIfAbsent(time, t -> newSnapshotMap());
        for (var e : values.entrySet()) {
            var nk = NormalizedProperties.getNormalized(e.getKey());
            if (nk == null) continue;
            snap.put(nk, pool.get(nk, e.getValue()));
        }
    }

    @Override public void putTimelineValues(Instant time, LinkedHashMap<String, TimelineValue> values) {
        var snap = timeline.computeIfAbsent(time, t -> newSnapshotMap());
        for (var e : values.entrySet()) {
            var nk = NormalizedProperties.getNormalized(e.getKey()); // keep normalization consistent
            if (nk == null) continue;
            snap.put(nk, e.getValue());
        }
    }

    @Override public Map<String, TimelineValue> snapshot(Instant time) {
        var m = timeline.get(time);
        // Return a stable, unmodifiable snapshot view (copy = iteration-safe for UI)
        return m == null ? Map.of() : Map.copyOf(m);
    }

    @Override public NavigableSet<Instant> timestamps() {
        // Live view is mutable; wrap to avoid external mutation, remains navigable/ordered
        return Collections.unmodifiableNavigableSet(timeline.navigableKeySet());
    }

    @Override public void update(Instant t, Map<String, TimelineValue> values) {
        if (t == null || values == null || values.isEmpty()) return;
        var snap = timeline.get(t);
        if (snap == null) return;

        for (var e : values.entrySet()) {
            var nk = NormalizedProperties.getNormalized(e.getKey());
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

    @Override public void updateStrings(Instant t, Map<String, String> values) {
        if (t == null || values == null || values.isEmpty()) return;
        var snap = timeline.get(t);
        if (snap == null) return;

        for (var e : values.entrySet()) {
            var nk = NormalizedProperties.getNormalized(e.getKey());
            if (nk == null) continue;

            var raw = e.getValue();
            if (raw == null || raw.isBlank() || "-".equals(raw)) {
                snap.remove(nk);
            } else {
                snap.put(nk, pool.get(nk, raw));  // canonicalize via pool
            }
        }
    }

    @Override public void upsert(Instant t, String key, TimelineValue value) {
        if (t == null) return;
        var nk = NormalizedProperties.getNormalized(key);
        if (nk == null) return;

        var snap = timeline.get(t);
        if (snap == null) return;

        var v = (value == null ? null : value.value());
        if (v == null || v.isBlank() || "-".equals(v)) {
            snap.remove(nk);
        } else {
            snap.put(nk, value);
        }
    }

    @Override public void remove(Instant t, String key) {
        if (t == null) return;
        var nk = NormalizedProperties.getNormalized(key);
        if (nk == null) return;

        var snap = timeline.get(t);
        if (snap != null) snap.remove(nk);
    }

}
