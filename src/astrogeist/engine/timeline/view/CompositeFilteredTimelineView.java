package astrogeist.engine.timeline.view;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.NavigableSet;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.abstraction.TimelineViewFilter;
import astrogeist.engine.timeline.TimelineValue;

public final class CompositeFilteredTimelineView  implements TimelineView {
	private final ArrayList<TimelineView> views = new ArrayList<>();
	
	public CompositeFilteredTimelineView(TimelineView baseView) { 
		this.views.add(baseView);
		
		// For testing:
		pushFilter(new PropertyEqualsTimelineViewFilter("Telescope", "Seestar 50 Sun"));
	}

	@Override public final Map<String, TimelineValue> snapshot(Instant time) {
		var last = this.views.getLast();
		return last == null ? null : last.snapshot(time);
	}

	@Override public final NavigableSet<Instant> timestamps() {
		var last = this.views.getLast();
		return last == null ? null : last.timestamps();
	}
	
	public final int getFilterCount() { return this.views.size() - 1; }
	
	public final void pushFilter(TimelineViewFilter filter) {
		var last = this.views.getLast();
		var newLast = new FilteredTimelineView(last, filter);
		views.add(newLast);
	}
	
	public final void popFilter() {
		var n = this.views.size();
		if (n == 1) return; // throw?
		this.views.remove(n - 1);
	}

}
