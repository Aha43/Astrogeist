package astrogeist.engine.abstraction;

/**
 * <p>
 *   Interface for
 *   {@link Scanner} that can be loaded, requires having a constructor taking the 
 *   location string.
 * </p>
 */
public interface PluginScanner extends Scanner {
	String location();
}
