package astrogeist.engine.resources;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import astrogeist.Common;

public final class Time {
	private Time() { Common.throwStaticClassInstantiateError(); }
	
	public static final DateTimeFormatter TimeFormatter = 
		DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));
}
