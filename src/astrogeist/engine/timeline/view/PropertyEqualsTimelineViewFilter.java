package astrogeist.engine.timeline.view;

import java.time.Instant;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.abstraction.TimelineViewFilter;

public record PropertyEqualsTimelineViewFilter(String key, String searched) implements TimelineViewFilter {
	@Override public final boolean accept(Instant time, TimelineView view) {
		var snapshot = view.snapshot(time);
		if (snapshot == null) return false;
		var tlv = snapshot.get(key);
        return tlv != null && searched.equals(tlv.value());
	}
	
	@Override public final String toString() { return key + " = " + searched; }
}
