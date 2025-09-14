package astrogeist.engine.abstraction;

/**
 * <p>
 *   Interface for
 *   {@link Scanner} that can be loaded.
 * </p>
 * <p>
 *   Requires having a constructor taking the 
 *   {@link #location()} string.
 * </p>
 */
public interface PluginScanner extends Scanner {
	/**
	 * <p>
	 *   Gets the location.
	 * </p>
	 * <p>
	 *   This is typical a path to folder to scan but could be 
	 *   something else (i.e. a connection string or URI).
	 * </p>
	 * @return the location.
	 */
	String location();
}
