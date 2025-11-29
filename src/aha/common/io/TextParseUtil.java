package aha.common.io;

import aha.common.guard.Guards;
import aha.common.util.Strings;

/**
 * <p>
 *   Methods of use when parsing text files.
 * </p>
 */
public final class TextParseUtil {
	private TextParseUtil() { Guards.throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Strips comment (text that start with '#') out of the passed line.
	 * </p>
	 * @param line the line.
	 * @return the line stripped of comment text.
	 */
	public static String stripComment(String line) {
        String trimmed = line.stripLeading();
        if (trimmed.isEmpty()) return Strings.EMPTY;
        int idx = trimmed.indexOf('#');
        if (idx == 0) return Strings.EMPTY;
        if (idx > 0) trimmed = trimmed.substring(0, idx);
        return trimmed.strip();
    }
}
