package astrogeist.engine.scanner.capdata;

import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import astrogeist.engine.logging.Log;

public class DefaultUtcExtractor implements UtcExtractor  {
	private final Logger logger = Log.get(this);
	
	@Override
	public Instant extract(Path path) {
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
                return instant;
            } catch (DateTimeException e) {
            	this.logger.log(Level.WARNING, "Failed to extract time for path: " + path.toString());
            }
        } else {
        	this.logger.log(Level.WARNING, "Failed to extract time for path: " + path.toString());
        }
        
        return null;
	}

}
