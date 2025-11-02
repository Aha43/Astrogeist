package aha.common;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   Methods to safely work with values that may be {@code null} if then should
 *   work with a default (i.e. empty collection type).
 * </p>
 */
public final class Safe {
	private Safe() { Guards.throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Returns the empty 
	 *   {@code String} if {@code s == null}.
	 * </p>
	 * @param s the {@code string} to access safely.
	 * @return {@code String} to access.
	 */
	public final static String string(String s) { return string(s, null); }
	
	/**
	 * <p>
	 *   Returns the empty 
	 *   {@code String} (case where {@code c == null}) or a 
	 *   {@code String} of an optional character if {@code s == null}.
	 * </p>
	 * @param s the {@code string} to access safely.
	 * @param c the character to return as a 
	 *          {@code String} if {@code s == null}, may be {@code null}
	 * @return {@code String} to access.
	 */
	public final static String string(String s, Character c) {
		return s == null ? (c == null ? "" : c.toString()) : s; } 
	
	/**
	 * <p>
	 *   Returns the empty 
	 *   {@code String} array if passed array is {@code null}.
	 * </p>
	 * @param a Passed array.
	 * @return Array to access.
	 */
	public final static String[] array(String[] a) {
		return a == null ? Empty.StringArray : a; }
	
	/**
	 * <p>
	 *   Returns a empty collection id passed collection is {@code null}.
	 * </p>
	 * @param c Passed collection.
	 * @return Collection to access.
	 */
	public final static Collection<String> collection(Collection<String> c) {
		return c == null ? List.of() : c; }

}
