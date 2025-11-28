package astrogeist.engine.abstraction.timeline;

import java.nio.file.Path;

import astrogeist.engine.timeline.TimelineValue;

/**
 * <p>
 *   Interface for object that pool time line values.
 * </p>
 * <p>
 *   So since astrogeist is using this the value "1000" for say framecount will only
 *   be stored once even if appear multiple times in time line snapshots.  
 * </p>
 */
public interface TimelineValuePool {
	/**
	 * <p>
	 *   Gets the named
	 *   {@link TimelineValue}.
	 * </p>
	 * @param name  the name of the value.
	 * @param value the v
	 * @return
	 */
	TimelineValue get(String name, String value);
	TimelineValue getFileValue(Path path);
}
