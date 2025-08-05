package astrogeist.engine.util;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;

import astrogeist.Common;

public final class Instants {
	public static String toFileSafeString(Instant instant) { return FORMATTER.format(instant); }

    public static Instant fromFileSafeString(String input) { return Instant.from(PARSER.parse(input)); }
	
	private static final DateTimeFormatter FORMATTER =
	        DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH-mm-ss.SSSX") // Note: ':' replaced with '-' for filesystem safety
	                          .withZone(ZoneOffset.UTC);

	    private static final DateTimeFormatter PARSER =
	        DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH-mm-ss.SSSX")
	                         .withZone(ZoneOffset.UTC);

	private Instants() { Common.throwStaticClassInstantiateError(); }
}
