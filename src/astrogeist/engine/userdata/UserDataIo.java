package astrogeist.engine.userdata;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.resources.Resources;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.util.io.NameValueMapXml;

public final class UserDataIo {
	private final TimelineValuePool timelineValuePool;
	
	public UserDataIo(TimelineValuePool timelineValuePool) {
		this.timelineValuePool = timelineValuePool; }
	
	public final LinkedHashMap<String, TimelineValue> load(Instant t) throws Exception {
		var file = Resources.getUserDataFile(t);
		if (!file.exists()) {
			file.createNewFile();
		}
		var retVal = NameValueMapXml.loadTimeLineValues(this.timelineValuePool, file);
		return retVal;
	}
	
	public final void save(Instant t, LinkedHashMap<String, TimelineValue> userData) throws Exception {
		var file = Resources.getUserDataFile(t);
		var valuesToSave = new LinkedHashMap<>(userData);
		removeDeleted(valuesToSave);
		NameValueMapXml.saveTimelineValues(valuesToSave, file);
	}
	
	private static void removeDeleted(LinkedHashMap<String, TimelineValue> userData) {
		var keys = new ArrayList<String>();
		for (var e : userData.entrySet()) {
			if (e.getValue().noData()) keys.add(e.getKey());
		}
		for (var k : keys) userData.remove(k);
	}
}
