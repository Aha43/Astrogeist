package astrogeist.engine.userdata;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import astrogeist.Common;
import astrogeist.engine.resources.Resources;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.util.io.NameValueMapXml;

public final class UserDataIo {
	public static LinkedHashMap<String, TimelineValue> load(Instant t) throws Exception {
		var file = Resources.getUserDataFile(t);
		if (!file.exists()) {
			file.createNewFile();
		}
		var retVal = NameValueMapXml.loadTimeKineValues(file);
		return retVal;
	}
	
	public static void save(Instant t, LinkedHashMap<String, TimelineValue> userData) throws Exception {
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

	private UserDataIo() { Common.throwStaticClassInstantiateError(); }
	
}
