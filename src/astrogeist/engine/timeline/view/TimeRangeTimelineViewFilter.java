package astrogeist.engine.timeline.view;

import java.time.Instant;

import astrogeist.engine.abstraction.TimelineView;

public final class TimeRangeTimelineViewFilter extends AbstractTimelineViewFilter {
	private final Instant from;
	private final Instant to;
	
	public TimeRangeTimelineViewFilter(Instant to, Instant from) {
		this.from = from;
		this.to = to;
	}
	
	public final Instant from() { return this.from; }
	public final Instant to() { return this.to; }
	
	@Override public boolean accept(Instant time, TimelineView view) {
		return (from == null || !time.isBefore(from)) && (to == null || !time.isAfter(to)); }
	
	@Override public String toString() {
		return "Time " + (from==null?"(-∞)":from) + " → " + (to==null?"(+∞)":to); }
}
