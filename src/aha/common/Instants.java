package aha.common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneOffset;

/**
 * <p>
 *   Utility methods of use when working with
 *   {@link Instant}s.
 * </p>
 */
public final class Instants {
	private Instants() { Guards.throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Gets a
	 *   {@link String} that can be used as a file name that communicate the
	 *   given time.
	 * </p>
	 * @param instant the time.
	 * @return File name safe
	 *         {@link String} based on {@code }.
	 * @see #fromFileSafeString(String)
	 */
	public final static String toFileSafeString(Instant t) {
		return FORMATTER.format(t); }

	/**
	 * <p>
	 *   Gets the time encoded in a
	 *   {@link String} produced by
	 *   {@link #toFileSafeString(Instant)}.
	 * </p>
	 * @param input {@link String} to get time from.
	 * @return Time.
	 * @see #toFileSafeString(Instant)
	 */
    public static Instant fromFileSafeString(String input) {
    	return Instant.from(PARSER.parse(input)); }
	
    // Note: ':' replaced with '-' for filesystem safety
	private static final DateTimeFormatter FORMATTER =
		DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH-mm-ss.SSSX").
			withZone(ZoneOffset.UTC);

	private static final DateTimeFormatter PARSER =
		DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH-mm-ss.SSSX").
			withZone(ZoneOffset.UTC);
	    
	/**
	 * <p>
	 *   Gets the
	 *   {@link Interval} a
	 *   {@link String} in the ISO date (day) represents.
	 * </p>
	 * @param isoDate Date
	 *                {@link String}.
	 * @return the {@link Interval} of the day.
	 */
	public static Interval dayInterval(String isoDate) {
        LocalDate date = LocalDate.parse(isoDate); // parse "YYYY-MM-DD"
        Instant from = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant to   = from.plus(1, java.time.temporal.ChronoUnit.DAYS);
        return new Interval(from, to);
    }
	
	private static final DateTimeFormatter ISO_DATE =
		DateTimeFormatter.ISO_LOCAL_DATE;
	
	/**
	 * <p>
	 *   Tells if a
	 *   {@link String} represents an ISO date or not.
	 * </p>
	 * @param s the {@link String} to test.
	 * @return {@code true} if is else {@code false}.
	 */
	public static boolean isIsoDate(String s) {
        if (s == null) return false;
        try {
            LocalDate.parse(s, ISO_DATE);
            return true;
        } catch (DateTimeParseException ex) { return false; }
    }
    
    /**
     * <p>
     *   Gets current date in ISO (yyyy-MM-dd), UTC.
     * </p>
     * @return Date string as specified.
     */
    public static String todayIsoUtc() {
        return LocalDate.now(ZoneOffset.UTC).toString(); }
     
    /**
     * <p>
     *   Gets current time in ISO (yyyy-MM-dd'T'HH:mm:ss'Z'), UTC 
     * </p>
     * @return ISO time.
     */
    public static String nowIsoUtcDateTime() { 
    	return Instant.now().toString(); }
    
    /**
     * <p>
     *   Represents an interval.
     * </p>
     */
    public record Interval(Instant from, Instant to) {
    	@Override public final String toString() { 
    		return "" + (from==null?"(-∞)":from) + " → " + 
    			(to==null?"(+∞)":to); }
    }
    
}
