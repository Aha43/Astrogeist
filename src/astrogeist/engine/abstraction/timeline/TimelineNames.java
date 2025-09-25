package astrogeist.engine.abstraction.timeline;

import java.util.Set;

/**
 * <p>
 *   Interface for objects that tell what data properties 
 *   scanned is named in source and is named in time line. 
 * </p>
 */
public interface TimelineNames {
	/**
	 * <p>
	 *   Get name used in time line given a name found in data.
	 * </p>
	 * @param key Name in data.
	 * @return Name in time line.
	 */
	public String getTimelineName(String key);
	
	/**
	 * <p>
	 *   Get all names used in time line mapped from scanned data.
	 * </p>
	 * @return Names.
	 */
	public Set<String> getDataTimelineNames();
	
	/**
	 * <p>
	 *   Get all names used in time line mapped from scanned data and
	 *   user data.
	 * </p>
	 * @return Names.
	 */
	public Set<String> getTimelineNames();
}
