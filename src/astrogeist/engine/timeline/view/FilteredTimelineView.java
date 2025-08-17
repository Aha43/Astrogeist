package astrogeist.engine.timeline.view;

import java.time.Instant;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.abstraction.TimelineViewFilter;
import astrogeist.engine.timeline.TimelineValue;

public final class FilteredTimelineView implements TimelineView {
	
	private TimelineView baseView;
	private final TimelineViewFilter filter;
	
	public FilteredTimelineView(TimelineView last, TimelineViewFilter filter) {
		this.baseView = last;
		this.filter = filter;
	}

	@Override public final Map<String, TimelineValue> snapshot(Instant time) {
		return this.baseView.snapshot(time); }

	@Override public final NavigableSet<Instant> timestamps() {
		var retVal = new TreeSet<Instant>();
		
		for (var ts : this.baseView.timestamps()) 
			if (this.filter.accept(ts, baseView)) retVal.add(ts);
		
		return retVal;
	}
	
	public final TimelineViewFilter getFilter() { return this.filter; }
	
	public final TimelineView getBaseView() { return this.baseView; }
	public final void rebase(TimelineView baseView) { this.baseView = baseView; }
}
