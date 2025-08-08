package astrogeist.engine.abstraction;

import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.typesystem.Type;

public interface Timeline {
	void clear();
	void put(Instant time, Path path);
	void put(Instant time, LinkedHashMap<String, String> values);
	void putTimelinesValues(Instant time, LinkedHashMap<String, TimelineValue> values);
	void put(Instant time, String key, String value);
    String get(Instant time, String key); 
    LinkedHashMap<String, TimelineValue> snapshot(Instant time);
    List<TimelineValue> getOfType(Instant time, Type type);
    Set<Instant> timestamps();
    //Issues getIssues();
}
