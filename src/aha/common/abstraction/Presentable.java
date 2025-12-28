package aha.common.abstraction;

/**
 * <p>
 *   Interface for objects that know how to present itself to end user.
 * </p>
 */
public interface Presentable {
	/**
	 * <p>
	 *   Gets a 
	 *   {@link String} that represent the object in a way suitable for the end
	 *   user to relate to.
	 * </p>
	 * <p>
	 *   If {@code toString()} return a nice presentation it is this can 
	 *   forward.
	 * </p>
	 * @return the representation.
	 */
	String presentation();
}
