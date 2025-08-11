package astrogeist.engine.abstraction;

import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import astrogeist.engine.timeline.TimelineValue;

public interface Timeline extends TimelineView {
	void clear();
	void put(Instant time, Path path);
	void put(Instant time, LinkedHashMap<String, String> values);
	void putTimelineValues(Instant time, LinkedHashMap<String, TimelineValue> values);
	void update(Instant t, Map<String, TimelineValue> values);   // add/update/remove
	void updateStrings(Instant t, Map<String, String> values);   // convenience
	void upsert(Instant t, String key, TimelineValue value);     // single key
	void remove(Instant t, String key);                          // delete single key
}
