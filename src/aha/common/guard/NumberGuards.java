package aha.common.guard;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;
import static aha.common.util.Strings.quote;
import static java.lang.Double.isFinite;
import static java.lang.Float.isFinite;
import static java.lang.Double.compare;

import java.util.Objects;

/**
 /**
 * <p>
 *   Methods to check for some number arguments requirements.
 * </p>
 * <p>
 *   Do <b>not duplicate</b> guards java comes with (i.e those in 
 *   {@link Objects}).
 * </p>
 */
public final class NumberGuards {
	private NumberGuards() { throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link IllegalArgumentException} if {@code value < 0}.
	 * </p>
	 * @param value the value to check.
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value < 0}.
	 */
	public static long requireNonNegative(long value) {
		if (value < 0)
			throw new IllegalArgumentException("must not be negative (is " +
				quote(value) + ")");
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
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value < 1}.
	 */
	public static long requirePositive(long value) {
		if (value < 1)
			throw new IllegalArgumentException("must be positive (is " + 
				quote(value) + ")");
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
	 *   Throws 
	 *   {@link IllegalArgumentException} if {@code value} is a NaN or infinite.
	 * </p>
	 * @param value the value to check.
	 * @param name  the name used in exception to refer to {@code value}
	 *              (typically a method parameter name).
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value} is not finite.
	 */
	public static double requireFinite(float value) {
	    if (!isFinite(value)) 
	    	throw new IllegalArgumentException("must be finite");
	    return value;
	}
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link IllegalArgumentException} if {@code value} is a NaN or infinite.
	 * </p>
	 * @param value the value to check.
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value} is not finite.
	 */
	public static double requireFinite(float value, String name) {
	    if (!isFinite(value))
	        throw new IllegalArgumentException(name + " must be finite");
	    return value;
	}
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link IllegalArgumentException} if {@code value} is a NaN or infinite.
	 * </p>
	 * @param value the value to check.
	 * @param name  the name used in exception to refer to {@code value}
	 *              (typically a method parameter name).
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value} is not finite.
	 */
	public static double requireFinite(double value) {
	    if (!isFinite(value)) 
	    	throw new IllegalArgumentException("must be finite");
	    return value;
	}
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link IllegalArgumentException} if {@code value} is a NaN or infinite.
	 * </p>
	 * @param value the value to check.
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value} is not finite.
	 */
	public static double requireFinite(double value, String name) {
	    if (!isFinite(value))
	        throw new IllegalArgumentException(name + " must be finite");
	    return value;
	}
	
	/**
	 * <p>
	 *   Throws
	 *   {@link IllegalArgumentException} if {@code value} is not equal or
	 *   larger than {@code min}.
	 * </p>
	 * @param value the value to test.
	 * @param min   the minimum value {@code value} can be.
	 * @return {@code value}.
	 * @throws IllegalArgumentException if {@code value < min}.
	 */
	public static double requireEqualOrLargerThan(double value, double min) {
		var r = compare(value, min);
		if (r < 0) throw new IllegalArgumentException(
			"must be equal to or larger than " + quote(min) + " but is " 
				+ quote(value));
		return value;
	}
	
	/**
	 * <p>
	 *   Throws
	 *   {@link IllegalArgumentException} if {@code value} is not equal or
	 *   larger than {@code min}.
	 * </p>
	 * @param value the value to test.
	 * @param min   the minimum value {@code value} can be.
	 * @param name  the name used in exception to refer to {@code value}
	 *              (typically a method parameter name).  
	 * @return {@code value}.
	 * @throws IllegalArgumentException if {@code value < min}.
	 */
	public static double requireEqualOrLargerThan(double value, double min,
		String name) {
		var r = compare(value, min);
		if (r < 0) throw new IllegalArgumentException(name +
			" must be equal to or larger than " + quote(min) + " but is " 
				+ quote(value));
		return value;
	}
	
	/**
	 * <p>
	 *   Throws
	 *   {@link IllegalArgumentException} if {@code value} is not equal or
	 *   less than {@code max}.
	 * </p>
	 * @param value the value to test.
	 * @param max   the maximum value {@code value} can be.
	 * @return {@code value}.
	 * @throws IllegalArgumentException if {@code value > max}.
	 */
	public static double requireEqualOrLesserThan(double value, double max) {
		var r = compare(value, max);
		if (r > 0) throw new IllegalArgumentException(
			" must be equal to or lesser than " + quote(max) + " but is " 
				+ quote(value));
		return value;
	}
	
	/**
	 * <p>
	 *   Throws
	 *   {@link IllegalArgumentException} if {@code value} is not equal or
	 *   less than {@code max}.
	 * </p>
	 * @param value the value to test.
	 * @param max   the maximum value {@code value} can be.
	 * @param name  the name used in exception to refer to {@code value}
	 *              (typically a method parameter name). 
	 * @return {@code value}.
	 * @throws IllegalArgumentException if {@code value > max}.
	 */
	public static double requireEqualOrLesserThan(double value, double max,
		String name) {
		var r = compare(value, max);
		if (r > 0) throw new IllegalArgumentException(name +
			" must be equal to or lesser than " + quote(max) + " but is " 
				+ quote(value));
		return value;
	}
	
}
