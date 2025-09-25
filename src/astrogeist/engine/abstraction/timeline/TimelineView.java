package astrogeist.engine.abstraction.timeline;

import java.time.Instant;
import java.util.Map;
import java.util.NavigableSet;

import astrogeist.engine.timeline.TimelineValue;

public interface TimelineView {
	Map<String, TimelineValue> snapshot(Instant time);
	NavigableSet<Instant> timestamps();
}
