package astrogeist.engine.timeline.view;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import astrogeist.engine.abstraction.timeline.TimelineView;
import astrogeist.engine.timeline.Snapshot;
import astrogeist.engine.timeline.TimelineValue;

public class FilteredTimeline implements TimelineView {
	// Chronological, thread-safe, navigable
    private ConcurrentNavigableMap<Instant,
    	Map<String, TimelineValue>> timeline =
        	new ConcurrentSkipListMap<>();
    
    public FilteredTimeline() {}

    public FilteredTimeline(
    	ConcurrentNavigableMap<Instant, 
    		Map<String, TimelineValue>> data) { 
    	this.timeline = data;
    }

    @Override public Snapshot snapshot(Instant time) {
        var m = timeline.get(time);
        return m == null ? Snapshot.Empty : new Snapshot(m);
    }

    @Override public NavigableSet<Instant> timestamps() {
        // Live view is mutable; wrap to avoid external mutation, remains
    	// navigable/ordered
        return Collections.unmodifiableNavigableSet(timeline.navigableKeySet());
    }
    
    @Override public String toString() { return this.timeline.toString(); }

}
