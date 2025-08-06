package astrogeist.engine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import astrogeist.Common;
import astrogeist.engine.typesystem.ParsedValue;

public final class Strings {
	public static List<String> fromCsv(String s) {
		var retVal = new ArrayList<>(Arrays.asList(s.split("\\s*,\\s*")));
		return retVal;
	}
	
	public static String toCsv(List<String> l) { return String.join(", ", l); }
	
	public static boolean isNullOrBlank(String s) { return s == null || s.trim().isEmpty(); }
	
	public static Optional<ParsedValue> parseNumberWithSuffix(String s) {
	    var pattern = Pattern.compile("^\\s*([+-]?\\d+(\\.\\d+)?)\\s*(.*)$");
	    var matcher = pattern.matcher(s);

	    if (matcher.matches()) {
	        double number = Double.parseDouble(matcher.group(1));
	        String suffix = matcher.group(3).trim();
	        return Optional.of(new ParsedValue(number, suffix));
	    }

	    return Optional.empty(); // not a valid number at the beginning
	}
	
	private Strings() { Common.throwStaticClassInstantiateError(); }
}
