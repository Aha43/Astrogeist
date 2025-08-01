package astrogeist.scanner;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import astrogeist.Common;
import astrogeist.resources.Resources;
import astrogeist.userdata.UserDataDefinitions;

public final class NormalizedProperties {
	static {
		_mapping = new LinkedHashMap<>();
		
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
		AddMapping("Subject");
		AddMapping("Telescope");
	}
	
	private static final LinkedHashMap<String, String> _mapping;
	
	public static void AddMapping(String normalized, String ...synonyms) {
		_mapping.put(normalized, normalized);
		if (synonyms != null)
			for (var s : synonyms) { _mapping.put(s, normalized); }
	}
	
	public static String getNormalized(String key) { return _mapping.get(key); }
	
	public static Set<String> getNormalizedNames() { return _mapping.keySet(); }
	
	public static Set<String> getNormalizedNamesAndUserDataNames() {
		try {
			var retVal = new HashSet<String>();
			retVal.addAll(getNormalizedNames());
			var userDataNames = UserDataDefinitions.fromXml(Resources.getUserDataDefinitionsFile().toPath()).getUserDataNames();
			retVal.addAll(userDataNames);
			return retVal;
		} catch (Exception x) {
			System.err.println(x.getLocalizedMessage());
			return new HashSet<String>();
		}
		
	}
	
	private NormalizedProperties() { Common.throwStaticClassInstantiateError(); }
}
