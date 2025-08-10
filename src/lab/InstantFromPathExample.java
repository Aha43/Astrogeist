package lab;

import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstantFromPathExample {
    public static void main(String[] args) {
        Path path = Path.of("/Users/arnehalvorsen/SharpCap/2025-05-04/Sun/ASIVideoStack_Output/15_20_51_Surface_50.fit");

        String normalizedPath = path.toString().replace('\\', '/');

        // Match date (YYYY-MM-DD)
        Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher dateMatcher = datePattern.matcher(normalizedPath);

        // Match time (HH_mm_ss)
        Pattern timePattern = Pattern.compile("(\\d{2})_(\\d{2})_(\\d{2})");
        Matcher timeMatcher = timePattern.matcher(normalizedPath);

        if (dateMatcher.find() && timeMatcher.find()) {
            String date = dateMatcher.group(); // e.g. 2025-05-04
            String hour = timeMatcher.group(1);
            String minute = timeMatcher.group(2);
            String second = timeMatcher.group(3);

            String dateTimeStr = date + "T" + hour + ":" + minute + ":" + second;
            try {
                LocalDateTime ldt = LocalDateTime.parse(dateTimeStr);
                Instant instant = ldt.atZone(ZoneId.of("UTC")).toInstant();
                System.out.println("✅ Extracted Instant: " + instant);
            } catch (DateTimeException e) {
                System.out.println("⚠️ Failed to parse datetime: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ Could not extract both date and time from path.");
        }
    }
}


