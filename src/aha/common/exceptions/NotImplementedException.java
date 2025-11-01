package aha.common.exceptions;

/**
 * <p>
 *   {@link UnsupportedOperationException} exception to throw when method not
 *   implemented.
 * </p>
 */
public final class NotImplementedException
	extends UnsupportedOperationException {
	
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 */
	public NotImplementedException() { super("Not implemented"); }
}
