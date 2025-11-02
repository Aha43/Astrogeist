package aha.common;

/**
 * <p>
 *   Utility class providing safe and expressive type casting operations.
 * </p>
 * <p>
 *   Inspired by C#'s {@code as} and {@code is} operators, 
 *   {@link #as(Class, Object)} performs a null-safe cast that returns 
 *   {@code null} if incompatible, while
 *   {@link #is(Object, Class)} checks whether an object is an instance of a 
 *   given type.
 * </p>
 */
public final class Cast {
	private Cast() { Guards.throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
     *   Safe cast similar to C# "as" operator.
     * </p>
	 * @param <T> the type to cast to.
	 * @param t   the class of the type to cast to.
	 * @param o   the object to cast.
	 * @return the object cast to the given type, or null if it’s not
	 *         compatible.
	 */
	public final static <T> T as(Class<T> t, Object o) { 
		return is(o, t) ? t.cast(o) : null; }
	
	/**
	 * <p>
     *   Safe cast similar to C# "as" operator throws an exception if can not
     *   cast.
     * </p> 
	 * @param <T> the type to cast to.
	 * @param t   the class of the type to cast to.
	 * @param o   the object to cast.
	 * @return the object cast to the given type.
	 * @throws ClassCastException if it's not compatible.
	 */
	public final static <T> T asOrThrow(Class<T> t, Object o) {
        if (!is(o, t))
            throw new ClassCastException("Cannot cast " + (o == null ? 
            	"null" : o.getClass().getName()) + " to " + t.getName());
        return t.cast(o);
    }
	
	/**
	 * <p>
     *   Type check similar to C# "is" operator.
     * </p> 
	 * @param o the object to check.
	 * @param t the type to check.
	 * @return {@code true} if the object is an instance of the given type.
	 */
    public final static boolean is(Object o, Class<?> t) {
    	return t.isInstance(o); }  
}
