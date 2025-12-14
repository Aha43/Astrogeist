package aha.common.guard;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;
import static aha.common.util.Strings.quote;

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
	 *   {@link IllegalArgumentException} if {@code value} is not a number
	 *   (NaN).
	 * </p>
	 * @param value the value to check.
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value < 1}.
	 */
	public static float requireNumber(float value) {
		if (Float.isNaN(value))
			throw new IllegalArgumentException("is not an number");
		return value;
	}
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link IllegalArgumentException} if {@code value} is not a number
	 *   (NaN).
	 * </p>
	 * @param value the value to check.
	 * @param name  the name used in exception to refer to {@code value}
	 *              (typically a method parameter name). 
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value < 1}.
	 */
	public static float requireNumber(float value, String name) {
		if (Float.isNaN(value))
			throw new IllegalArgumentException(name + " is not an number");
		return value;
	}
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link IllegalArgumentException} if {@code value} is not a number
	 *   (NaN).
	 * </p>
	 * @param value the value to check.
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value < 1}.
	 */
	public static double requireNumber(double value) {
		if (Double.isNaN(value))
			throw new IllegalArgumentException("is not an number");
		return value;
	}
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link IllegalArgumentException} if {@code value} is not a number
	 *   (NaN).
	 * </p>
	 * @param value the value to check.
	 * @param name  the name used in exception to refer to {@code value}
	 *              (typically a method parameter name). 
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value < 1}.
	 */
	public static double requireNumber(double value, String name) {
		if (Double.isNaN(value))
			throw new IllegalArgumentException(name + " is not an number");
		return value;
	}
	
}
