package aha.common.abstraction;

/**
 * <p>
 *   Interface for named objects.
 * </p>
 */
public interface Named {
	/**
	 * <p>
	 *   Gets name.
	 * </p>
	 * @return the name.
	 */
	String name();
	
	/**
	 * <p>
	 *   Gets name usable for end user presentation.
	 * </p>
	 * @return the display name.
	 */
	String displayName();
}
