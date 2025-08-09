package astrogeist.engine.abstraction;

import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableSet;

import astrogeist.engine.timeline.TimelineValue;

public interface Timeline {
	void clear();
	void put(Instant time, Path path);
	void put(Instant time, LinkedHashMap<String, String> values);
	void putTimelineValues(Instant time, LinkedHashMap<String, TimelineValue> values);
	Map<String, TimelineValue> snapshot(Instant time);
	NavigableSet<Instant> timestamps();
}
