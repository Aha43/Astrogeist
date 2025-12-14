package astrogeist.engine.scanner;

import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aha.common.logging.Log;
import astrogeist.engine.abstraction.UtcExtractor;

public final class DefaultUtcExtractor implements UtcExtractor  {
	private final Logger logger = Log.get(this);
	
	@Override public final Instant extract(Path path) {
		var normalizedPath = path.toString().replace('\\', '/');

        // Match date (YYYY-MM-DD)
        var datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        var dateMatcher = datePattern.matcher(normalizedPath);

        // Match time (HH_mm_ss)
        var timePattern = Pattern.compile("(\\d{2})_(\\d{2})_(\\d{2})");
        Matcher timeMatcher = timePattern.matcher(normalizedPath);

        if (dateMatcher.find() && timeMatcher.find()) {
            var date = dateMatcher.group(); // e.g. 2025-05-04
            var hour = timeMatcher.group(1);
            var minute = timeMatcher.group(2);
            var second = timeMatcher.group(3);

            var dateTimeStr = date + "T" + hour + ":" + minute + ":" + second;
           
            try {
                var ldt = LocalDateTime.parse(dateTimeStr);
                var instant = ldt.atZone(ZoneId.of("UTC")).toInstant();
                return instant;
            } catch (DateTimeException e) {
            	this.logger.log(Level.WARNING, 
            		"Failed to extract time for path: " + path.toString());
            }
        } else {
        	this.logger.log(Level.WARNING, "Failed to extract time for path: " +
        		path.toString());
        }
        
        return null;
	}

}
