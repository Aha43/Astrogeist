package astrogeist.engine.abstraction;

public interface Scanner {
	
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
	 *   Scan.
	 * </p> 
	 * @param timeline {@link Timeline} to add scanned information to.
	 * @throws Exception If fails for any reason.
	 */
	void scan(Timeline timeline) throws Exception;
	
	/**
	 * <p>
	 *   The shared empty array.
	 * </p>
	 */
	Scanner[] EmptyArray = new Scanner[0];
}
