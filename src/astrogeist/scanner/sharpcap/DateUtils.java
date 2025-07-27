package astrogeist.scanner.sharpcap;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class DateUtils {
	public static LocalDate tryParseDate(String input) {
        try {
            return LocalDate.parse(input); // expects format yyyy-MM-dd
        } catch (DateTimeParseException e) {
            return null; // return null if parsing fails
        }
    }
	
	private DateUtils() { throw new AssertionError("Can not instantiate utility class"); }
}
