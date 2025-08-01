package astrogeist.scanner.regex;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimestampRegexResolver extends RegexExtractor<Instant> {
    public TimestampRegexResolver(String regex, String format, String timezone) {
        super(regex, matcher -> {
            String ts = matcher.group(1); // assume 1 group
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(ts, fmt).atZone(ZoneId.of(timezone)).toInstant();
        });
    }
}




