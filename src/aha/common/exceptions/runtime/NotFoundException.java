package aha.common.exceptions.runtime;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.util.Strings.quote;

/**
 * <p>
 *   Thrown when something required to exist is not found.
 * </p>
 */
public final class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 */
	public NotFoundException() {}
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param name the name on what is not found.
	 * @throws IllegalArgumentException if {@code msg} is {@code null} or is the
	 *         empty string.
	 */
	public NotFoundException(String name) {
		super(quote(requireNonEmpty(name, "name")) + " not found"); }
}
