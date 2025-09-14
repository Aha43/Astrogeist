package astrogeist.engine.abstraction;

import astrogeist.engine.abstraction.jobs.JobWorker;

/**
 * <p>
 *   A component that 
 * </p>
 */
public interface Scanner extends JobWorker<Timeline> {
	/**
	 * <p>
	 *   Name suitable for displaying to end user.
	 * </p>
	 * @return the name
	 */
	String name();
	
	/**
	 * <p>
	 *   Description on what scanner does.
	 * </p>
	 * @return the description
	 */
	String description();
	
	/**
	 * <p>
	 *   The shared empty array.
	 * </p>
	 */
	Scanner[] EmptyArray = new Scanner[0];
}
