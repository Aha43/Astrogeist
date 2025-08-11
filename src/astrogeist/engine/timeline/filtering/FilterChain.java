package astrogeist.engine.timeline.filtering;

import java.util.ArrayList;
import java.util.List;

import astrogeist.engine.abstraction.TimelineSnapshotFilter;
import astrogeist.engine.abstraction.TimelineView;

public final class FilterChain {
    private final List<TimelineSnapshotFilter> filters = new ArrayList<>();

    public void add(TimelineSnapshotFilter f) { filters.add(f); }
    public void remove(int index) { filters.remove(index); }
    public void clear() { filters.clear(); }
    public List<TimelineSnapshotFilter> list() { return List.copyOf(filters); }

    public TimelineView apply(TimelineView base) {
        if (filters.isEmpty()) return base;
        return new FilteredTimelineView(base, filters);
    }
}

