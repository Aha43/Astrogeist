package astrogeist.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import astrogeist.Common;

public final class Strings {
	public static List<String> fromCsv(String s) {
		var retVal = new ArrayList<>(Arrays.asList(s.split("\\s*,\\s*")));
		return retVal;
	}
	
	public static String toCsv(List<String> l) { return String.join(", ", l); }
	
	private Strings() { Common.throwStaticClassInstantiateError(); }
}
