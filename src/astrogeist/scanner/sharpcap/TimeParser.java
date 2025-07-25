package astrogeist.scanner.sharpcap;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

public final class TimeParser {

    public static Instant parseTimeFromFilename(String timePart, LocalDate date) {
        try {
            String[] parts = timePart.split("\\.");
            if (parts.length == 0) return null;

            String timeString = parts[0].replace("Z", "").replace('_', ':');
            LocalTime time = LocalTime.parse(timeString);
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            return dateTime.toInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            return null;
        }
    }
    
    private TimeParser() { throw new AssertionError("Cannot instantiate utility class"); }
    
}
