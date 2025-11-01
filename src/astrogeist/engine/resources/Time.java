package astrogeist.engine.resources;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import aha.common.Guards;

public final class Time {
	private Time() { Guards.throwStaticClassInstantiateError(); }
	
	public static final DateTimeFormatter TimeFormatter = 
		DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));
}
