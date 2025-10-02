package astrogeist.engine.resources;

import java.awt.Color;
import java.util.Map;

import astrogeist.common.Common;
import astrogeist.engine.typesystem.Type;

public final class FileTypeColorMap { 
	private FileTypeColorMap() { Common.throwStaticClassInstantiateError(); }

	public static final Map<Type.DiskFile, Color> EXTENSION_COLORS = Map.ofEntries(
	        Map.entry(Type.FitFile(), new Color(255, 250, 205)),      // fit - light yellow (LemonChiffon)
	        Map.entry(Type.TifFile(), new Color(224, 255, 255)),      // tif - light cyan (LightCyan)
	        Map.entry(Type.JpgFile(), new Color(255, 228, 225)),      // jpg - misty rose
	        Map.entry(Type.PngFile(), new Color(230, 230, 250)),      // png - lavender
	        Map.entry(Type.TxtFile(), new Color(245, 245, 245))       // txt - light gray
	    );
}
