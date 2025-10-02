package astrogeist.engine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import astrogeist.common.Common;

public final class Strings {
	private Strings() { Common.throwStaticClassInstantiateError(); }
	
	public static List<String> fromCsv(String s) {
		var retVal = new ArrayList<>(Arrays.asList(s.split("\\s*,\\s*")));
		return retVal;
	}
	
	public static String toCsv(List<String> l) { return String.join(", ", l); }
	
	public static boolean isNullOrBlank(String s) { return s == null || s.trim().isEmpty(); }
	
	public static String nullToEmpty(String s) { return s == null ? "" : s; }

	public static String nullToEmptyTrimmed(String s) { return s == null ? "" : s.trim(); }

	public static String safeToString(Object o) {
	    return o == null ? "" : nullToEmptyTrimmed(o.toString()); }

	
	public static Optional<ParsedSuffixNumberValue> parseNumberWithSuffix(String s) {
	    // number = [+|-] digits [ '.' or ',' digits ], then optional suffix
	    var pattern = Pattern.compile("^\\s*([+-]?\\d+(?:[\\.,]\\d+)?)\\s*(.*)$");
	    var matcher = pattern.matcher(s);

	    if (!matcher.matches()) return Optional.empty();

	    String numberPart = matcher.group(1).replace(',', '.'); // normalize
	    String suffix = matcher.group(2).trim();

	    try {
	        double number = Double.parseDouble(numberPart);
	        return Optional.of(new ParsedSuffixNumberValue(number, suffix));
	    } catch (NumberFormatException e) {
	        return Optional.empty();
	    }
	}

}
