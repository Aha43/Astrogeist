package astrogeist.common;

/**
 * <p>
 *   Place to put shared immutable empty instances of common types. 
 * </p>
 * <p>
 *   Shared immutable empty instances of any implemented types should
 *   be put in the interface, record or class declaration.
 * </p>
 */
public interface Empty {
	/**
	 * <p>
	 *   The empty
	 *   {@link String} array.
	 * </p>
	 */
	String[] StringArray = new String[0];
}
