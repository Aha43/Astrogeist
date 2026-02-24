package astrogeist.engine.scanner;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import aha.common.abstraction.io.appdata.AppDataManager;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.engine.appdata.userdatadefinitions.UserDataDefinitions;
import astrogeist.engine.observatory.Observatory;

public final class DefaultTimelineNames implements TimelineNames {
	private final LinkedHashMap<String, String> mapping = new LinkedHashMap<>();
	
	private final AppDataManager astrogeistStorageManager;
	
	public DefaultTimelineNames(
		AppDataManager astrogeistStorageManager,
		Observatory observatory) {
		
		requireNonNull(astrogeistStorageManager, "astrogeistStorageManager");
		requireNonNull(observatory, "observatory");
		
		this.astrogeistStorageManager = astrogeistStorageManager;
		
		addMapping("Binning");
		addMapping("Camera");
		addMapping("CaptureSoftware", "SharpCap");
		addMapping("CaptureSoftwareVersion", "SharpCapVersion");
		addMapping("Duration");
		addMapping("Exposure");
		addMapping("FrameCount");
		addMapping("Gain");
		addMapping("Stars");
		addMapping("SerFile");
		addMapping("Seeing");
		addMapping("FitFile");
		addMapping("TifFile", "TiffFile");
		addMapping("JpgFile");
		addMapping("PngFile");
		addMapping("Temperature");
		addMapping("Resolution", "Capture Area");
		addMapping("Subject");
		addMapping("Telescope");
		addMapping("CaptureType", "FrameType");
		
		for (var curr : observatory.getAxisNames()) addMapping(curr);
	}
	
	private final void addMapping(String normalized, String ...synonyms) {
		this.mapping.put(normalized, normalized);
		if (synonyms != null)
			for (var s : synonyms) this.mapping.put(s, normalized);
	}
	
	public final String getTimelineName(String key) { 
		return this.mapping.get(key); 
	}
	
	public final Set<String> getDataTimelineNames() {
		return this.mapping.keySet(); }
	
	public final Set<String> getTimelineNames() {
		try {
			var retVal = new HashSet<String>();
			retVal.addAll(this.getDataTimelineNames());
			
			var userDataDefs = this.astrogeistStorageManager.load(
				UserDataDefinitions.class);
			var userDataNames = userDataDefs.getUserDataNames();
			
			retVal.addAll(userDataNames);
			return retVal;
		} catch (Exception x) {
			System.err.println(x.getLocalizedMessage());
			return new HashSet<String>();
		}
	}
	
}
