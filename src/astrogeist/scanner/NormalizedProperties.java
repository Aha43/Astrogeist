package astrogeist.scanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class NormalizedProperties {
	static {
		_mapping = new HashMap<>();
		
		AddMapping("Binning");
		AddMapping("Camera");
		AddMapping("CaptureSoftware", "SharpCap");
		AddMapping("CaptureSoftwareVersion", "SharpCapVersion");
		AddMapping("Duration");
		AddMapping("Exposure");
		AddMapping("FrameCount");
		AddMapping("Gain");
		AddMapping("SerFile");
		AddMapping("FitFile");
		AddMapping("TifFile", "TiffFile");
		AddMapping("JpgFile");
		AddMapping("PngFile");
		AddMapping("Temperature");
	}
	
	private static final Map<String, String> _mapping;
	
	public static void AddMapping(String normalized, String ...synonyms) {
		_mapping.put(normalized, normalized);
		if (synonyms != null)
			for (var s : synonyms) { _mapping.put(s, normalized); }
	}
	
	public static String getNormalized(String key) { return _mapping.get(key); }
	
	public static Set<String> getNormalizedNames() { return _mapping.keySet(); }
	
	private NormalizedProperties() { throw new AssertionError("Can not instantiate static class"); }
}
