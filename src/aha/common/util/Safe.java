package aha.common.util;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   Methods to safely work with values that may be {@code null} if then should
 *   work with a default (i.e. empty collection type).
 * </p>
 */
public final class Safe {
	private Safe() { throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Returns the empty 
	 *   {@code String} if {@code s == null}.
	 * </p>
	 * @param s the {@code string} to access safely.
	 * @return {@code String} to access.
	 */
	public final static String string(String s) { return s == null ? "" : s; }
	
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
	 *   {@code String} (case where {@code c == null}) or a 
	 *   {@code String} of an optional string if {@code s == null}.
	 * </p>
	 * @param s the {@code string} to access safely.
	 * @param d the {@code String} to return as a 
	 *          if {@code s == null}, may be {@code null}.
	 * @return {@code String} to access.
	 */
	public final static String string(String s, String d) {
		return s == null ? (d == null ? "" : d) : s; }
	
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
	 *   Returns an empty collection id passed collection is {@code null}.
	 * </p>
	 * @param c Passed collection.
	 * @return Collection to access.
	 */
	public final static Collection<String> collection(Collection<String> c) {
		return c == null ? List.of() : c; }
	
	/**
	 * <p>
	 *   If {@code o == null} returns 'null' else 
	 *   {@code o.getClass().toString()}. 
	 * </p>
	 * @param o Object to get class name for.
	 * @return the string 'null' if {@code o == null} else the class name of
	 *         {@code o}. 
	 */
	public final static String className(Object o) {
		return o == null ? "null" : o.getClass().toString(); }

}
