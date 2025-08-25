package astrogeist.engine.timeline.view.filters;

import java.time.Instant;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.timeline.view.AbstractTimelineViewFilter;
import astrogeist.engine.util.Instants.Interval;

public final class TimeRangeTimelineViewFilter extends AbstractTimelineViewFilter {
	private final Interval interval;
	
	public TimeRangeTimelineViewFilter(Interval interval) { this.interval = interval; }
	
	public final Interval interval() { return this.interval; }
	public final Instant from() { return this.interval.from(); }
	public final Instant to() { return this.interval.to(); }
	
	@Override public final boolean accept(Instant time, TimelineView view) {
		var from = this.from();
		var to = this.to();
		return (from == null || !time.isBefore(from)) && (to == null || !time.isAfter(to)); }
	
	@Override public final String toString() { return this.interval.toString(); }
}
