package aha.common.util;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;
import static aha.common.util.Strings.isNullOrBlank;

public final class Default {
	private Default() { throwStaticClassInstantiateError(); }
	
	public static <T> T orDefault(T v, T d){ return v == null ? v : d; }

	public static String orDefault(String v, String d) {
		return isNullOrBlank(v) ? d : v; }
}
