package astrogeist.engine.abstraction;

import java.time.Instant;
import java.util.Map;
import java.util.NavigableSet;

import astrogeist.engine.timeline.TimelineValue;

public interface TimelineView {
	NavigableSet<Instant> timestamps();
    Map<String, TimelineValue> snapshot(Instant time);
}
