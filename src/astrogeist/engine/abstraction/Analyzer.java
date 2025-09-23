package astrogeist.engine.abstraction;

/**
 * <p>
 *   Interface for components that analyze snapshots of a
 *   {@link Timeline}.
 * </p>
 */
public interface Analyzer {
	/**
	 * <p>
	 *   Analyze.
	 * </p>
	 * @param timeline the {@link Timeline} to analyze.
	 */
	void analyze(Timeline timeline);
	
	/**
	 * <p>
	 *   Gets number of snapshots that will be analyzed.
	 * </p>
	 * @return Number or {@code -1} if unknown.
	 */
	int size();
}
