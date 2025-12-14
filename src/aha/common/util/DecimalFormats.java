package aha.common.util;

import java.text.DecimalFormat;

import aha.common.guard.ObjectGuards;

/**
 * <p>
 *   Shared decimal formats.
 * </p>
 */
public final class DecimalFormats {
	private DecimalFormats() { ObjectGuards.throwStaticClassInstantiateError(); }
	
	public static final DecimalFormat DF6 = new DecimalFormat("0.000000");
	public static final DecimalFormat DF3 = new DecimalFormat("0.000");
}
