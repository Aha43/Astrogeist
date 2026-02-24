package astrogeist.engine.abstraction.timeline;

import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import astrogeist.engine.timeline.TimelineValue;

/**
 * <p>
 *   Defines the mutable time line, extends the read only time line view.
 * </p>
 */
public interface Timeline extends TimelineView {
	/**
	 * <p>
	 *   Removes all data.
	 * </p>
	 */
	void clear();
	
	void put(Instant time, Path path);
	void put(Instant time, String name, String value);
	void put(Instant time, LinkedHashMap<String, String> values);
	void putTimelineValues(Instant time, Map<String, TimelineValue> values);
	
	/**
	 * <p>
	 *   Add/update/remove.
	 * </p>
	 * @param t
	 * @param values
	 */
	void update(Instant t, Map<String, TimelineValue> values); 
	
	/**
	 * <p>
	 *   Convenience.
	 * </p>
	 * @param t
	 * @param values
	 */
	void updateStrings(Instant t, Map<String, String> values); 
	
	/**
	 * <p>
	 *   Single key.
	 * </p>
	 * @param t
	 * @param key
	 * @param value
	 */
	void upsert(Instant t, String key, TimelineValue value);
	
	/**
	 * <p>
	 *   Delete single key.
	 * </p>
	 * @param t
	 * @param key
	 */
	void remove(Instant t, String key);
	
}
