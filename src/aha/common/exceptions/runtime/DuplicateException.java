package aha.common.exceptions.runtime;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.util.Strings.quote;

/**
 * <p>
 *   Thrown when something that is required to be unique is found not to be.
 * </p>
 */
public final class DuplicateException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 */
	public DuplicateException() {}
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param name the name on what is found to be a duplicate.
	 * @throws IllegalArgumentException if {@code msg} is {@code null} or is the
	 *         empty string.
	 */
	public DuplicateException(String name) {
		super("duplicate : " + quote(requireNonEmpty(name, "name"))); }
}
