package astrogeist.engine.abstraction;

/**
 * <p>
 *   Interface for objects that scan for time line data.
 * </p>
 */
public interface Scanner {
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
