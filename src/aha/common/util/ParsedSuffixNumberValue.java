package aha.common.util;

/**
 * <p>
 *   Represents a number parsed from a string where only the first part of the
 *   string may have parsed into a number.
 * </p>
 * @see Strings#parseNumberWithSuffix(String)
 */
public record ParsedSuffixNumberValue(
	/**
	 * <p>
     *   The numerical value parsed from the beginning of the input string.
     *   If no number was found at the start, this may be NaN (Not-a-Number) or
     *   0.0, depending on the parsing logic used.
     * </p>
     */
	double number,
	
	 /**
	 * <p>
     *   The remainder of the string after the number was parsed, which serves
     *   as a suffix. This value may be an empty string if the entire input 
     *   string was parsed as a number.
     * </p>
     */
	String suffix) {}
