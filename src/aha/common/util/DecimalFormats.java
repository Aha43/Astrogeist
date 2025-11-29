package aha.common.util;

import java.text.DecimalFormat;

import aha.common.guard.Guards;

/**
 * <p>
 *   Shared decimal formats.
 * </p>
 */
public final class DecimalFormats {
	private DecimalFormats() { Guards.throwStaticClassInstantiateError(); }
	
	public static final DecimalFormat DF6 = new DecimalFormat("0.000000");
	public static final DecimalFormat DF3 = new DecimalFormat("0.000");
}
