package astrogeist.engine.scanner;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import astrogeist.engine.abstraction.persistence.AstrogeistStorageManager;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.engine.persitence.disk.userdatadefinitions.UserDataDefinitions;

public final class DefaultTimelineNames implements TimelineNames {
	private final LinkedHashMap<String, String> mapping = new LinkedHashMap<>();
	
	private final AstrogeistStorageManager astrogeistStorageManager;
	
	public DefaultTimelineNames(AstrogeistStorageManager astrogeistStorageManager) {
		this.astrogeistStorageManager = astrogeistStorageManager; 
		
		AddMapping("Binning");
		AddMapping("Camera");
		AddMapping("CaptureSoftware", "SharpCap");
		AddMapping("CaptureSoftwareVersion", "SharpCapVersion");
		AddMapping("Duration");
		AddMapping("Exposure");
		AddMapping("FrameCount");
		AddMapping("Gain");
		AddMapping("Stars");
		AddMapping("SerFile");
		AddMapping("Seeing");
		AddMapping("FitFile");
		AddMapping("TifFile", "TiffFile");
		AddMapping("JpgFile");
		AddMapping("PngFile");
		AddMapping("Temperature");
		AddMapping("Resolution", "Capture Area");
		AddMapping("Subject");
		AddMapping("Telescope");
		AddMapping("CaptureType", "FrameType");
	} 
	
	private void AddMapping(String normalized, String ...synonyms) {
		this.mapping.put(normalized, normalized);
		if (synonyms != null)
			for (var s : synonyms) this.mapping.put(s, normalized);
	}
	
	public String getTimelineName(String key) { return this.mapping.get(key); }
	
	public Set<String> getDataTimelineNames() { return this.mapping.keySet(); }
	
	public Set<String> getTimelineNames() {
		try {
			var retVal = new HashSet<String>();
			retVal.addAll(this.getDataTimelineNames());
			
			var userDataDefs = this.astrogeistStorageManager.load(UserDataDefinitions.class);
			var userDataNames = userDataDefs.getUserDataNames();
			
			retVal.addAll(userDataNames);
			return retVal;
		} catch (Exception x) {
			System.err.println(x.getLocalizedMessage());
			return new HashSet<String>();
		}
	}
	
}
