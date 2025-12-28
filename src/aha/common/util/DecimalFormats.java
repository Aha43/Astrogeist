package aha.common.util;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;

import java.text.DecimalFormat;

/**
 * <p>
 *   Shared decimal formats.
 * </p>
 */
public final class DecimalFormats {
	private DecimalFormats() { throwStaticClassInstantiateError(); }
	
	public static final DecimalFormat DF6 = new DecimalFormat("0.000000");
	public static final DecimalFormat DF3 = new DecimalFormat("0.000");
}
