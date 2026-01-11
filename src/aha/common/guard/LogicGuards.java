package aha.common.guard;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;

public final class LogicGuards {
	private LogicGuards() { throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link RuntimeException} if given boolean value is {@code true}.
	 * </p>
	 * @param v   the value to check.
	 * @param msg the error message.
	 */
	public final static void throwIf(boolean v, String msg) {
		if (v) throw new RuntimeException(msg); }
	
	/**
	 * <p>
	 *   Throws 
	 *   {@link RuntimeException} if given boolean value is {@code true}.
	 * </p>
	 * @param v   the value to check.
	 * @param msg the error message.
	 */
	public final static void throwIfNot(boolean v, String msg) {
		if (!v) throw new RuntimeException(msg); }
}
