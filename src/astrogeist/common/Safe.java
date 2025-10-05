package astrogeist.common;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   Methods to safely work with values that may be {@code null} if then should work with a default
 *   (i.e. empty collection type).
 * </p>
 */
public final class Safe {
	private Safe() { Guards.throwStaticClassInstantiateError(); }
	
	public static final String string(String s) { return string(s, null); }
	
	public static final String string(String s, Character c) { return s == null ? (c == null ? "" : c.toString()) : s; } 
	
	/**
	 * <p>
	 *   Returns the empty 
	 *   {@code String} array if passed array is {@code null}.
	 * </p>
	 * @param a Passed array.
	 * @return Array to access.
	 */
	public static final String[] array(String[] a) { return a == null ? Empty.StringArray : a; }
	
	/**
	 * <p>
	 *   Returns a empty collection id passed collection is {@code null}.
	 * </p>
	 * @param c Passed collection.
	 * @return Collection to access.
	 */
	public static Collection<String> collection(Collection<String> c) { return c == null ? List.of() : c; }
}
