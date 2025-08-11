package astrogeist.engine.timeline.filtering;

import java.time.Instant;
import java.util.Map;

import astrogeist.engine.abstraction.TimelineSnapshotFilter;
import astrogeist.engine.timeline.TimelineValue;

public record TimeRangeFilter(Instant from, Instant to) implements TimelineSnapshotFilter {
	@Override public boolean accepts(Instant time, Map<String, TimelineValue> snapshot) {
		return (from == null || !time.isBefore(from)) && (to == null || !time.isAfter(to)); }

	@Override public String label() {
		return "Time " + (from == null ? "(-∞)" : from) + " → " + (to == null ? "(+∞)" : to); }
}
