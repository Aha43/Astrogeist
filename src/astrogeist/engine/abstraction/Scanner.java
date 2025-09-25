package astrogeist.engine.abstraction;

import astrogeist.engine.abstraction.jobs.JobWorker;
import astrogeist.engine.abstraction.timeline.Timeline;

/**
 * <p>
 *   {@link JobWorker} that "scans" and add observation data to
 *   {@link Timeline}.
 * </p>
 */
public interface Scanner extends JobWorker<Timeline> {
	/**
	 * <p>
	 *   Tells if can scan.
	 * </p>
	 * @return {@code true} if can; {@code false} if can not.
	 */
	boolean canScan();
	
	/**
	 * <p>
	 *   Name suitable for displaying to end user.
	 * </p>
	 * @return the name
	 */
	String name();
	
	/**
	 * <p>
	 *   Description on what scanner does including why can not if
	 *   {@link #canScan()} is {@code false}.
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
