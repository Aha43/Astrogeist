package astrogeist.engine.timeline.view;

import java.time.Instant;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.abstraction.TimelineViewFilter;

public record TimeRangeTimelineViewFilter(Instant from, Instant to) implements TimelineViewFilter {
	@Override public boolean accept(Instant time, TimelineView view) {
		return (from == null || !time.isBefore(from)) && (to == null || !time.isAfter(to)); }
	
	@Override public String toString() {
		return "Time " + (from==null?"(-∞)":from) + " → " + (to==null?"(+∞)":to); }
}
