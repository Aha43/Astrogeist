package aha.common;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *   Place for static utility methods that implements runtime checks. Do not
 *   duplicate guards java comes with (i.e those in 
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
	 */
	public final static void throwStaticClassInstantiateError() {
		throw new AssertionError("Can not instantiate static class"); }
	
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
	public final static String requireNonEmpty(String value, String name) {
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
	public final static long requireNonNegative(long value, String name) {
		if (value < 0)
			throw new IllegalArgumentException(name +
				" must not be negative (is '" + value + "')");
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
	public final static long requirePositive(long value, String name) {
		if (value < 1)
			throw new IllegalArgumentException(name +
				" must be positive (is '" + value + "')");
		return value;
	}
	
	/**
	 * <p> 
	 *   Throws
	 *   {@link IllegalArgumentException} if map does not have given key.
	 * </p>
	 * @param <K> the key type.
	 * @param <V> the value type.
	 * @param map the map.
	 * @param key the key.
	 * @return the {@code map}.
	 */
	public final static <K, V> Map<K, V> requireKeyExists(Map<K, V> map, K key){
		return requireKeyExists(map, key, null); }
	
	/**
	 * <p> 
	 *   Throws
	 *   {@link IllegalArgumentException} if map does not have given key.
	 * </p>
	 * @param <K> the key type.
	 * @param <V> the value type.
	 * @param map the map.
	 * @param key the key.
	 * @param name the name used in exception to refer to {@code key} 
	 *             (typically a method parameter name).
	 * @return the {@code map}.
	 */
	public final static <K, V> Map<K, V> requireKeyExists(Map<K, V> map, K key,
		String name) {
		
		if (map.containsKey(key)) return map;
		name = (name == null) ? "key" : name;
		throw new IllegalArgumentException(name + " not found");
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
	public final static String ensureFileNameValid(String s) {
		if (Strings.isValidFileName(s)) return s.trim();
		throw new IllegalArgumentException("'" + s + "' not valid file name");
	}
	
}
