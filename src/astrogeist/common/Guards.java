package astrogeist.common;

/**
 * <p>
 *   Place for static utility methods used in across all packages.
 * </p>
 * <p>
 *   Should be very general programming tasks, utility methods for a given type
 *   (i.e. String) should be put in a separate utility class for that type.
 * </p>
 */
public final class Guards {
	private Guards() { throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link IllegalArgumentException} if 
	 *   {@code value} is the empty string (only blank characters or of length zero)
	 *   or {@code null}.   
	 * </p>
	 * @param value the value to check.
	 * @param name  the name used in exception to refer to {@code value} (typically a method parameter name).
	 * @return the {@code value}.
	 * @throws IllegalArgumentException If {@code value} is {@code null}, of length zero or composed of blank characters only.
	 */
	public static String requireNonEmpty(String value, String name) {
	    if (value == null || value.isEmpty())
	        throw new IllegalArgumentException(name + " must not be null or empty");
	    return value;
	}
	
	/**
	 * <p>
	 *   Should be called in the private constructor of static classes.
	 * </p>
	 * <p>
	 *   It does guard against the very unlikely case of invoking the private constructor but also
	 *   make code more explicit that this is a class that only should have static methods.
	 * </p>
	 */
	public static void throwStaticClassInstantiateError() { throw new AssertionError("Can not instantiate static class"); }
	
}
