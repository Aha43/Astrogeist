package aha.common.exceptions.runtime;

/**
 * <p>
 *   Thrown when attempt to write to some read only resource been done.
 * </p>
 */
public final class ReadOnlyException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>
	 *   Creates exception with no message.
	 * </p>
	 */
	public ReadOnlyException() {}
	
	/**
	 * <p>
	 *   Creates exception with message.
	 * </p>
	 * @param msg the error message.
	 */
	public ReadOnlyException(String msg) { super(msg); }
}
