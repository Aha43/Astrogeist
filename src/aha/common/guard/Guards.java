package aha.common.guard;

import static aha.common.util.Strings.isValidFileName;
import static java.util.Objects.requireNonNull;
import static aha.common.util.Strings.quote;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static aha.common.util.FilesUtil.getExtension;
import static aha.common.util.Strings.isNullOrBlank;

/**
 * <p>
 *   Place for static utility methods that implements runtime checks.
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
     *   Requires that the collection itself is non-null and that all elements 
     *   are non-null.
     * </p>
     * @return the {@code value}.
     * @throws NullPointerException If {@code value} is {@code null} or contain
     *         an element that is {@code null}.
     */
	public final static <T, C extends Collection<T>> C requireAllNonNull(
		C value, String name) {
	    
		requireNonNull(value, name);
	    int i = 0;
	    for (T element : value) {
	        if (element == null)
	            throw new NullPointerException(name + "[" + i + "] is null");
	        i++;
	    }
	    return value;
	}
	
	/**
	 * <p>
     *   Requires that the array itself is non-null and that all elements are
     *   non-null.
     * </p>
     * @return the {@code value}.
     * @throws NullPointerException If {@code value} is {@code null} or contain
     *         an element that is {@code null}. 
     */
	public static <T> T[] requireAllNonNull(T[] value, String name) {
        requireNonNull(value, name);
        for (int i = 0; i < value.length; i++) {
            if (value[i] == null)
                throw new NullPointerException(name + "[" + i + "] is null");
        }
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
		if (isValidFileName(s)) return s.trim();
		throw new IllegalArgumentException("'" + s + "' not valid file name");
	}
	
	public static File requireFileHasExtension(File file, String ext) {
	    requireNonNull(file, "file");
	    requireNonEmpty(ext, "ext");

	    if (!file.exists()) {
	        throw fileException(file, " does not exist");
	    }
	    if (file.isDirectory()) {
	        throw fileException(file, " is a directory");
	    }

	    // Normalize expected extension: remove leading dot and lower-case
	    String expected = ext.startsWith(".")
	            ? ext.substring(1)
	            : ext;
	    expected = expected.toLowerCase();

	    String fext = getExtension(file); // assuming this returns WITHOUT dot
	    if (isNullOrBlank(fext)) {
	        throw fileException(file, " is missing an extension");
	    }

	    String actual = fext.toLowerCase();
	    if (!actual.equals(expected)) {
	        throw fileException(
	                file,
	                " has not extension " + quote(expected) + " but " + quote(fext)
	        );
	    }

	    return file;
	}

	private static RuntimeException fileException(File file, String msg) {
	    return new IllegalArgumentException("file " + quote(file) + msg);
	}

}
