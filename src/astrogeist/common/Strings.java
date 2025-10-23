package astrogeist.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * <p>
 *   Utility methods of use when working with
 *   {@code String}.
 * </p>
 */
public final class Strings {
	private Strings() { Guards.throwStaticClassInstantiateError(); }
	
	public static final List<String> fromCsv(String s) {
		var retVal = new ArrayList<>(Arrays.asList(s.split("\\s*,\\s*")));
		return retVal;
	}
	
	public static final String toCsv(List<String> l) { return String.join(", ", l); }
	
	public static final boolean isNullOrBlank(String s) { return s == null || s.trim().isEmpty(); }
	
	public static final String nullToEmpty(String s) { return s == null ? "" : s; }

	public static final String nullToEmptyTrimmed(String s) { return s == null ? "" : s.trim(); }
	
	public static final Optional<ParsedSuffixNumberValue> parseNumberWithSuffix(String s) {
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
	
	// Windows forbidden characters: \ / : * ? " < > |
    private static final Pattern INVALID_WINDOWS_CHARS =
    	Pattern.compile("[\\\\/:*?\"<>|]");

    // Windows reserved device names (case-insensitive)
    private static final String[] RESERVED_WINDOWS_NAMES = {
        "CON", "PRN", "AUX", "NUL",
        "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
        "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"
    };
	
	/**
	 * <p>
     *   Checks if the provided string can safely be used as a file name
     *   across Windows, macOS, and Linux.
     * </p>
     * <p>
     *   Leading periods (like ".gitignore") are allowed.
     * </p>
     */
    public static final boolean isValidFileName(String name) {
        if (name == null || name.isBlank()) return false;

        if (name.equals(".") || name.equals("..")) return false;

        if (INVALID_WINDOWS_CHARS.matcher(name).find()) return false;

        String upper = name.toUpperCase();
        for (String reserved : RESERVED_WINDOWS_NAMES) {
            if (upper.equals(reserved) || upper.startsWith(reserved + "."))
                return false;
        }

        // Disallow trailing space or period (Windows limitation)
        if (name.endsWith(" ") || name.endsWith(".")) return false;

        // Length limit (255 chars is the common denominator)
        if (name.length() > 255) return false;

        return true;
    }
	
	public static final String ensureFileNameValid(String s) {
		if (isValidFileName(s)) return s.trim();
		throw new IllegalArgumentException("'" + s + "' not valid file name");
	}

}
