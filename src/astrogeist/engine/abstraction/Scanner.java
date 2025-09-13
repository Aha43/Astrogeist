package astrogeist.engine.abstraction;

import astrogeist.engine.abstraction.jobs.JobWorker;

public interface Scanner extends JobWorker<Timeline> {
	/**
	 * <p>
	 *   Name suitable for displaying to end user.
	 * </p>
	 */
	String name();
	
	/**
	 * <p>
	 *   Description on what scanner does.
	 * </p>
	 */
	String description();
	
	/**
	 * <p>
	 *   The shared empty array.
	 * </p>
	 */
	Scanner[] EmptyArray = new Scanner[0];
}
