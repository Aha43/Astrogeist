package astrogeist.ui.swing.tool.sun.sketching;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import astrogeist.common.Common;

public final class SunSketchFileNamer {
	private SunSketchFileNamer() { Common.throwStaticClassInstantiateError(); }
	
    private static final DateTimeFormatter SAFE_ISO =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss'Z'")
                         .withZone(ZoneOffset.UTC);

    public final static String xmlName(Instant utc) {
        return "SunSketch_" + SAFE_ISO.format(utc) + "_v1.xml"; }
}
