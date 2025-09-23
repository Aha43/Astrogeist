package astrogeist.engine.abstraction;

import java.nio.file.Path;

import astrogeist.engine.timeline.TimelineValue;

/**
 * <p>
 *   Interface for object that pool time line values.
 * </p>
 * <p>
 *   So since Astrogeist is using this the value "1000" for say framecount will only
 *   be stored once even if appear multiple times in time line snapshots.  
 * </p>
 */
public interface TimelineValuePool {
	/**
	 * <p>
	 *   Gets the shared
	 *   {@link TimelineValue} associated with given property name and value. 
	 * </p>
	 * @param name  the name of the property.
	 * @param value the value of the property.
	 * @return the shared
	 *         {@link TimelineValue}; may have been created by this call if 
	 *         one did not exist for the property name / value pair.
	 */
	TimelineValue get(String name, String value);
	
	/**
	 * <p>
	 *   Gets the shared
	 *   {@link TimelineValue} associated with given
	 *   {@link Path}. 
	 * </p> 
	 * @param path the path.
	 * @return the shared
	 *         {@link TimelineValue}; may have been created by this call if 
	 *         one did not exist for the path.
	 */
	TimelineValue getFileValue(Path path);
}
