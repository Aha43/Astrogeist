package astrogeist.engine.abstraction;

import java.time.Instant;
import java.util.LinkedHashMap;

import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.timeline.TimelineValue;

public interface TimelineManager {
	void timeline(Timeline timeline);
	void update(Instant t, LinkedHashMap<String, TimelineValue> values);
	void update(Instant t, String name, TimelineValue value);
}
