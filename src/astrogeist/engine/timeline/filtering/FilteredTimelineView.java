package astrogeist.engine.timeline.filtering;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import astrogeist.engine.abstraction.TimelineSnapshotFilter;
import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.timeline.TimelineValue;

public final class FilteredTimelineView implements TimelineView {
    private final TimelineView parent;
    private final List<TimelineSnapshotFilter> filters;
    private NavigableSet<Instant> cached; // lazy

    public FilteredTimelineView(TimelineView parent, List<TimelineSnapshotFilter> filters) {
        this.parent = parent;
        this.filters = java.util.List.copyOf(filters);
    }

    @Override
    public NavigableSet<Instant> timestamps() {
        if (cached == null) {
            var out = new TreeSet<Instant>();
            for (var t : parent.timestamps()) {
                var snap = parent.snapshot(t);
                boolean ok = true;
                for (var f : filters) {
                    if (!f.accepts(t, snap)) { ok = false; break; }
                }
                if (ok) out.add(t);
            }
            cached = out;
        }
        return cached;
    }

    @Override public Map<String, TimelineValue> snapshot(Instant time) {
    	return parent.snapshot(time); }
   
    public List<TimelineSnapshotFilter> filters() { return filters; }
}

