package aha.common.guard;

import static java.util.Objects.requireNonNull;

import aha.common.exceptions.runtime.SelfReferenceException;

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
public final class ObjectGuards {
	private ObjectGuards() { throwStaticClassInstantiateError(); }
	
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
	
}
