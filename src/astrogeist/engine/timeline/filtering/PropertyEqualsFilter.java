package astrogeist.engine.timeline.filtering;

import java.time.Instant;
import java.util.Map;

import astrogeist.engine.abstraction.TimelineSnapshotFilter;
import astrogeist.engine.timeline.TimelineValue;

public record PropertyEqualsFilter(String key, String expected) implements TimelineSnapshotFilter {
	@Override public boolean accepts(Instant time, Map<String, TimelineValue> snapshot) {
		var tv = snapshot.get(key);
		return tv != null && expected.equals(tv.value());
	}

	@Override public String label() { return key + " = " + expected; }
}
