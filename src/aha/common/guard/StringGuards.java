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
	 *   {@link NullPointerException} if {@code value} is {@code null} or
	 *   {@link IllegalArgumentException} if {@code value} is the empty string
	 *   (only blank characters or of length zero).   
	 * </p>
	 * @param value the value to check.
	 * @param name  the name used in exception to refer to {@code value}
	 *              (typically a method parameter name).
	 * @return the {@code value}.
	 * @throws NullPointerException if {@code value} is {@code null}.
	 * @throws IllegalArgumentException If {@code value} is of length zero or
	 *         composed of blank characters only.
	 */
	public static String requireNonEmpty(String value, String name) {
		if (value == null) throw new NullPointerException(name);
	    if (value.isEmpty())
	    	throw new IllegalArgumentException(name + " must not be empty");
	    return value;
	}
	
	/**
	 * <p>
	 *   Ensures that the given string does not contain any of the specified
	 *   forbidden characters.
	 * </p>
	 * <p>
	 *   This is typically used to enforce lexical constraints on identifiers
	 *   that will be embedded in structured formats (such as signatures,
	 *   configuration keys, or DSL-like strings).
	 * </p>
	 * @param forbiddenChars a string whose characters are all forbidden
	 * @param s              the string to validate.
	 * @param name           the logical name of the value being validated (for
	 *                       error messages).
	 * @return {@code s}, if it contains none of the forbidden characters.
	 * @throws IllegalArgumentException if {@code forbiddenChars} or {@code s}
	 *         is {@code null} or the empty {@code String}.
	 * @throws IllegalArgumentException if {@code s} contains any forbidden 
	 *         character.
	 */
	public static String requireNotHaveAny(String forbiddenChars, String s,
		String name) {
		
		requireNonEmpty(forbiddenChars, "forbiddenChars");
		requireNonEmpty(s, name);

		for (int i = 0; i < forbiddenChars.length(); i++) {
			char c = forbiddenChars.charAt(i);
			if (s.indexOf(c) >= 0) {
				throw new IllegalArgumentException(name +
					" must not contain '" + c + "': " + quote(s));
		    }
		}
		return s;
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
