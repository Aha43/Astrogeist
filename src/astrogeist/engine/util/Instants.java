package astrogeist.engine.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneOffset;

import astrogeist.Common;

public final class Instants {
	private Instants() { Common.throwStaticClassInstantiateError(); }
	
	public static String toFileSafeString(Instant instant) { return FORMATTER.format(instant); }

    public static Instant fromFileSafeString(String input) { return Instant.from(PARSER.parse(input)); }
	
	private static final DateTimeFormatter FORMATTER =
			DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH-mm-ss.SSSX").withZone(ZoneOffset.UTC); // Note: ':' replaced with '-' for filesystem safety

	private static final DateTimeFormatter PARSER =
	        DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH-mm-ss.SSSX").withZone(ZoneOffset.UTC);
	    
	public static Interval dayInterval(String isoDate) {
        LocalDate date = LocalDate.parse(isoDate);              // parse "YYYY-MM-DD"
        Instant from = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant to   = from.plus(1, java.time.temporal.ChronoUnit.DAYS);
        return new Interval(from, to);
    }
	
	private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
	
	public static boolean isIsoDate(String s) {
        if (s == null) return false;
        try {
            LocalDate.parse(s, ISO_DATE);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
    
    /**
     * <p>
     *   Gets current date in ISO (yyyy-MM-dd), UTC
     * </p>
     * @return
     */
    public static String todayIsoUtc() {
        return LocalDate.now(ZoneOffset.UTC).toString(); }
     
    /**
     * <p>
     *   Gets current time in ISO (yyyy-MM-dd'T'HH:mm:ss'Z'), UTC 
     * </p>
     * @return ISO time.
     */
    public static String nowIsoUtcDateTime() { return Instant.now().toString(); }
    
    public record Interval(Instant from, Instant to) {
    	@Override public final String toString() { 
    		return "" + (from==null?"(-∞)":from) + " → " + (to==null?"(+∞)":to); }
    }
}
