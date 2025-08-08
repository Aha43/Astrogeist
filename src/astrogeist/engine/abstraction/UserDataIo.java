package astrogeist.engine.abstraction;

import java.time.Instant;
import java.util.LinkedHashMap;

import astrogeist.engine.timeline.TimelineValue;

public interface UserDataIo {
	LinkedHashMap<String, TimelineValue> load(Instant t) throws Exception;
	void save(Instant t, LinkedHashMap<String, TimelineValue> userData) throws Exception;
}
