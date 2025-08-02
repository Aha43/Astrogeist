package astrogeist.scanner.regex;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimestampRegexResolver extends RegexExtractor<Instant> {
    public TimestampRegexResolver(String regex, String timezone) {
        super(regex, matcher -> {
            try {
                String date = matcher.group(1); // e.g., "2025-05-04"
                String hour = matcher.group(2); // "15"
                String minute = matcher.group(3); // "20"
                String second = matcher.group(4); // "51"

                // Construct timestamp directly
                LocalDateTime dt = LocalDateTime.parse(
                    date + "T" + hour + ":" + minute + ":" + second
                );

                return dt.atZone(ZoneId.of(timezone)).toInstant();
            } catch (Exception e) {
                return null; // safe fallback for bad match
            }
        });
    }
}





