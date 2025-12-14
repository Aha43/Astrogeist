package aha.common.guard;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;
import static aha.common.util.Strings.isValidFileName;
import static aha.common.util.Strings.quote;

import java.util.Objects;

/**
 * <p>
 *   Place for static utility methods that implements runtime checks, special
 *   argument checks.
 * </p>
 * <p>
 *   Do <b>not duplicate</b> guards java comes with (i.e those in 
 *   {@link Objects}).
 * </p>
 */
public final class StringGuards {
	private StringGuards() { throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link IllegalArgumentException} if 
	 *   {@code value} is the empty string (only blank characters or of length
	 *   zero) or {@code null}.   
	 * </p>
	 * @param value the value to check.
	 * @param name  the name used in exception to refer to {@code value}
	 *              (typically a method parameter name).
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value} is {@code null}, of
	 *         length zero or composed of blank characters only.
	 */
	public static String requireNonEmpty(String value, String name) {
	    if (value == null || value.isEmpty())
	        throw new IllegalArgumentException(name +
	        	" must not be null or empty");
	    return value;
	}
	
	/**
     * <p>
     *   Guard for a
     *   {@link String} is a valid file name. 
     * </p>
     * @param s the {@link String} to test.
     * @return {@code s}.
     * @throws IllegalArgumentException if {@code s} not valid file name.
     */
	public static String ensureFileNameValid(String s) {
		if (isValidFileName(s)) return s.trim();
		throw new IllegalArgumentException(quote(s) + " not valid file name");
	}
	
}
