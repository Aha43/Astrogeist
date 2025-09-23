package astrogeist.engine.exception;

/**
 * <p>
 *   {@link Exception} to throw when in need to throw a specific application exception.
 * </p>
 */
public class AstrogeistException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param message Error message.
	 */
	public AstrogeistException(String message) { super(message); }
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param message Error message.
	 * @param inner   Inner
	 *                {@link Exception}.
	 */
	public AstrogeistException(String message, Throwable inner) { super(message, inner); }
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param inner Inner
	 *              {@link Exception}.
	 */
	public AstrogeistException(Exception inner) { super(inner); }
	
}
