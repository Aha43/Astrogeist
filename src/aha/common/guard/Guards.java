package aha.common.guard;

import static aha.common.util.Strings.isValidFileName;
import static aha.common.util.Strings.quote;
import static java.util.Objects.requireNonNull;

import aha.common.exceptions.runtime.SelfReferenceException;
import static aha.common.util.Strings.isNullOrBlank;

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
public final class Guards {
	private Guards() { throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Should be called in the private constructor of static classes.
	 * </p>
	 * <p>
	 *   It does guard against the very unlikely case of invoking the private 
	 *   constructor but also make code more explicit that this is a class that
	 *   only should have static methods.
	 * </p>
	 * <p>
	 *   Example of use is this class's source.
	 * </p>
	 */
	public final static void throwStaticClassInstantiateError() {
		throw new AssertionError("Can not instantiate static class"); }
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link RuntimeException} if given boolean value is {@code true}.
	 * </p>
	 * @param v   the value to check.
	 * @param msg the error message.
	 */
	public final static void throwIf(boolean v, String msg) {
		if (v) throw new RuntimeException(msg); }
	
	/**
	 * <p>
	 *   Ensures that {@code other} is not the same object reference as 
	 *   {@code thiz}.
	 * </p>
	 * <p>
	 *   Typical usage is to prevent self-references, e.g. when assigning a 
	 *   parent:
	 * </p>
	 * @param other the value being validated (usually the one that will be 
	 *              assigned).
	 * @param thiz  the reference {@code other} must not be identical to (often 
	 *              {@code this}).
	 * @return {@code other}.
	 * @throws NullPointerException   if {@code other} or {@code thiz} is
	 *                                {@code null}.
	 * @throws SelfReferenceException if {@code other == thiz}.
	 * @see #requireNotSame(Object, Object, String)
	 */
	public final static <T> T requireNotSame(T other, T thiz) {
		return requireNotSame(other, thiz, null); }
	
	/**
	 * <p>
	 *   Ensures that {@code other} is not the same object reference as 
	 *   {@code thiz}.
	 * </p>
	 * <p>
	 *   Typical usage is to prevent self-references, e.g. when assigning a 
	 *   parent:
	 * </p>
	 * <pre>{@code
	 *   this.parent = requireNotSame(parent, this, 
	 *     "An object cannot be its own parent");
	 * }</pre>
	 * @param other the value being validated (usually the one that will be 
	 *              assigned).
	 * @param thiz  the reference {@code other} must not be identical to (often 
	 *              {@code this}).
	 * @param msg   the detail message for the
	 *              {@link SelfReferenceException}.
	 * @return {@code other}.
	 * @throws NullPointerException   if {@code other} or {@code thiz} is
	 *                                {@code null}.
	 * @throws SelfReferenceException if {@code other == thiz}.
	 * @see #requireNotSame(Object, Object)
	 */
	public final static <T> T requireNotSame(T other, T thiz, String msg) {
		requireNonNull(other, "other");
		requireNonNull(thiz, "thiz");
		if (other == thiz) throw new SelfReferenceException(msg);
		return other; 
	}
	
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
	 *   Throws 
	 *   {@link IllegalArgumentException} if {@code value < 0}.
	 * </p>
	 * @param value the value to check.
	 * @param name  the name used in exception to refer to {@code value}
	 *              (typically a method parameter name).
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value < 0}.
	 */
	public static long requireNonNegative(long value, String name) {
		if (value < 0)
			throw new IllegalArgumentException(name +
				" must not be negative (is " + quote(value) + ")");
		return value;
	}
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link IllegalArgumentException} if {@code value < 1}.
	 * </p>
	 * @param value the value to check.
	 * @param name  the name used in exception to refer to {@code value}
	 *              (typically a method parameter name).
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value < 1}.
	 */
	public static long requirePositive(long value, String name) {
		if (value < 1)
			throw new IllegalArgumentException(name +
				" must be positive (is " + quote(value) + ")");
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
