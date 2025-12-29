package aha.common.exceptions.runtime;

import static aha.common.util.Strings.isNullOrBlank;

/**
 * <p>
 *   Thrown to indicate that an illegal self reference was detected.
 * </p>
 */
public class SelfReferenceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>
	 *   Creates exception with no message.
	 * </p>
	 */
	public SelfReferenceException() {}
	
	/**
	 * <p>
	 *   Creates exception with message.
	 * </p>
	 * @param msg the error message.
	 */
	public SelfReferenceException(String msg) {
		super(isNullOrBlank(msg) ? "Self reference not allowed" : msg); }
}
