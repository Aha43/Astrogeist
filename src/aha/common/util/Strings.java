package aha.common.util;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;
import static aha.common.guard.NumberGuards.requireNonNegative;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * <p>
 *   Utility methods of use when working with
 *   {@code String}s.
 * </p>
 */
public final class Strings {
	private Strings() { throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   The empty
	 *   {@link String} shared.
	 * </p>
	 */
	public static final String EMPTY = "";
	
	/**
	 * <p>
	 *   Gets token of a ',' separated list given as a
	 *   {@link String}: "aa,b bb, c" yield a list of strings:
	 *   {"aa", "b bb", "c"}. 
	 * </p>
	 * @param s {@link String} to parse.
	 * @return List of tokens.
	 * @see #toCsv(List)
	 */
	public final static List<String> fromCsv(String s) {
		var retVal = new ArrayList<>(Arrays.asList(s.split("\\s*,\\s*")));
		return retVal;
	}
	
	/**
	 * <p>
	 *   Converts a list of values to a single CSV-formatted line.
	 * <p>
	 * <p>
	 *   Each element is converted using {@link Object#toString()}, with
	 *   CSV escaping applied according to RFC&nbsp;4180:
	 *   <ul>
	 *     <li>
	 *       Values containing commas, quotes, or newlines are enclosed in
	 *       quotes.
	 *     </li>
	 *     <li>
	 *       Quotes inside a quoted value are doubled.
	 *     </li>
	 *   </ul>
	 * </p>
	 * <p>
	 *   The method accepts a list of any element type. {@code null} elements
	 *   are represented as empty CSV fields.
	 * </p>
	 * @param list the list of values to convert; may be {@code null} or empty
	 * @return a CSV-formatted string containing one field per list element,
	 *         or an empty string if the input is {@code null} or empty
	 */
	public final static String toCsv(List<?> list) {
	    if (list == null || list.isEmpty()) return "";

	    var sb = new StringBuilder();
	    var first = true;

	    for (var o : list) {
	        if (!first) {
	            sb.append(',');
	        }
	        first = false;

	        if (o == null) {
	            sb.append("");
	            continue;
	        }

	        String s = o.toString();

	        // Escape CSV if needed
	        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
	            sb.append('"');
	            sb.append(s.replace("\"", "\"\""));
	            sb.append('"');
	        } else {
	            sb.append(s);
	        }
	    }

	    return sb.toString();
	}

	/**
	 * <p>
	 *   Checks if a given string is null, empty, or consists only of whitespace
	 *   characters.
	 * </p>
	 * <p>
	 *   This method is a convenience utility that checks for {@code null}
	 *   first, and if not null, checks if the string is blank (contains only
	 *   whitespace) after trimming.
	 * </p>
	 * @param s The string to check.
	 * @return {@code true} if the string is null or blank, {@code false}
	 *         otherwise.
	 */
	public final static boolean isNullOrBlank(String s) { 
		return s == null || s.trim().isEmpty(); }
	
	/**
	 * <p>
	 *   Converts a null string reference to an empty string ("").
	 * </p>
	 * <p>
	 *   This safe access method prevents {@link NullPointerException} when
	 *   a string value is required but the input might be null.</p>
	 * </p>
	 * @param s The input string, which may be null.
	 * @return The original string if it is not null, otherwise an empty 
	 *         string ("").
	 */
	public final static String nullToEmpty(String s) { 
		return s == null ? "" : s; }

	/**
	 * <p>
	 *   Converts a null string reference to an empty string ("") after trimming
	 *   leading and trailing whitespace from the input if it is not null.
	 * </p>
	 * <p>
	 *   This is useful for normalizing input values, ensuring that the result
	 *   is never null and contains no surrounding whitespace.</p>
	 * </p>
	 * @param s The input string, which may be null.
	 * @return The trimmed original string if it is not null, otherwise an empty
	 *         string ("").
	 */
	public final static String nullToEmptyTrimmed(String s) { 
		return s == null ? "" : s.trim(); }
	
	/**
	 * <p>
     *   Attempts to parse a number from the beginning of a given string
     *   {@code s}, capturing the remaining text as a suffix.
     * </p>
     * <p>
     *   The parser supports a flexible format:
     *   {@code [whitespace][+|-][digits][.|,][digits][whitespace][suffix]}
     * </p>
     * <p>
     *   The input string is normalized internally to accept both comma (',')
     *   and period ('.') as decimal separators before parsing the number as a
     *   standard {@code double}.
     * </p>
     * @param s The input string to parse.
     * @return An 
     *         {@link Optional} containing a
     *         {@link ParsedSuffixNumberValue} if
     *         a number was successfully extracted from the start of the string,
     *         otherwise
     *         {@link Optional#empty()}.
     */
	public final static Optional<ParsedSuffixNumberValue> parseNumberWithSuffix(
		String s) {
	    
		// number = [+|-] digits [ '.' or ',' digits ], then optional suffix
	    var pattern =
	    	Pattern.compile("^\\s*([+-]?\\d+(?:[\\.,]\\d+)?)\\s*(.*)$");
	    var matcher = pattern.matcher(s);

	    if (!matcher.matches()) return Optional.empty();

	    String numberPart = matcher.group(1).replace(',', '.'); // normalize
	    String suffix = matcher.group(2).trim();

	    try {
	        var number = Double.parseDouble(numberPart);
	        return Optional.of(new ParsedSuffixNumberValue(number, suffix));
	    } catch (NumberFormatException e) {
	        return Optional.empty();
	    }
	}
	
	/**
	 * <p>
	 *   Windows forbidden characters: \ / : * ? " < > |
	 * </p>
	 */
    public static final Pattern INVALID_WINDOWS_CHARS =
    	Pattern.compile("[\\\\/:*?\"<>|]");

    /**
     * <p>
     *   Windows reserved device names (case-insensitive).
     * </p>
     */
    public static final String[] RESERVED_WINDOWS_NAMES = {
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
    public final static boolean isValidFileName(String name) {
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
	
    /**
     * <p>
     *   Truncates a
     *   {@link String} if longer than a given length.
     * </p>
     * @param s the {@link String} to truncate if longer than {@code s}.
     * @param l the length to truncate.
     * @return {@code s > l ? s.substring(0, l) : s}.
     * @throws IllegalArgumentException if {@code l < 0}.
     */
    public final static String truncate(String s, int l) {
    	requireNonNegative(l, "l");
    	return l == 0 ? EMPTY : (s.length() > l ? s.substring(0, l) : s);
    }
    
    /**
     * <p>
     *   Quotes.
     * </p>
     * @param o the object to quote.
     * @return the string {@code ' + o + '}
     */
    public final static String quote(Object o) { return "'" + o + "'"; }
    
    private final static String[] BLANKS = new String[] {
    	"",
    	" ",
    	"  ",
    	"   ",
    	"    ",
    	"     ",
    	"      ",
    	"       ",
    	"        ",
    	"         ",
    	"          ",
    	"           ",
    	"            ",
    	"             ",
    	"              ",
    	"               ",
    	"                ",
    	"                 ",
    	"                  ",
    	"                   ",
    	"                    ",
    	"                     ",
    	"                      ",
    	"                       ",
    	"                        ",
    	"                         ",
    	"                          ",
    };
    
    /**
     * <p>
     *   Gets the string with blanks only (' ') of given length.
     * </p>
     * @param n the required length.
     * @return the string.
     * @throws IllegalArgumentException if {@code n < 0}.
     */
    public final static String padding(int n) {
    	requireNonNegative(n, "n");
    	if (n < BLANKS.length) return BLANKS[n];
    	var sb = new StringBuilder(n);
    	for (var i = 0; i < n; i++) sb.append(' ');
    	return sb.toString();
    }

}
