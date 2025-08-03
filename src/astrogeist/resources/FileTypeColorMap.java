package astrogeist.resources;

import java.awt.Color;
import java.util.Map;

import astrogeist.Common;

public final class FileTypeColorMap {
	public static final Map<String, Color> EXTENSION_COLORS = Map.ofEntries(
	        Map.entry("fit", new Color(255, 250, 205)),      // light yellow (LemonChiffon)
	        Map.entry("fits", new Color(240, 255, 240)),     // light green (Honeydew)
	        Map.entry("tif", new Color(224, 255, 255)),      // light cyan (LightCyan)
	        Map.entry("tiff", new Color(224, 255, 255)),     // same as tif
	        Map.entry("jpg", new Color(255, 228, 225)),      // misty rose
	        Map.entry("png", new Color(230, 230, 250)),      // lavender
	        Map.entry("txt", new Color(245, 245, 245))       // light gray
	        // Add more as needed
	    );

	public FileTypeColorMap() { Common.throwStaticClassInstantiateError(); }
}
