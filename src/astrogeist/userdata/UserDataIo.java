package astrogeist.userdata;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import astrogeist.Common;
import astrogeist.Empty;
import astrogeist.app.resources.Resources;
import astrogeist.util.io.NameValueMapXml;

public final class UserDataIo {
	public static LinkedHashMap<String, String> load(Instant t) throws Exception {
		var file = Resources.getUserDataFile(t);
		if (!file.exists()) {
			file.createNewFile();
		}
		var retVal = NameValueMapXml.load(file);
		return retVal;
	}
	
	public static void save(Instant t, LinkedHashMap<String, String> userData) throws Exception {
		var file = Resources.getUserDataFile(t);
		var valuesToSave = new LinkedHashMap<>(userData);
		removeDeleted(valuesToSave);
		NameValueMapXml.save(valuesToSave,file);
	}
	
	private static void removeDeleted(LinkedHashMap<String, String> userData) {
		var keys = new ArrayList<String>();
		for (var e : userData.entrySet()) {
			var val = e.getValue();
			if (val == null || val == Empty.String || val.equals("-")) keys.add(e.getKey());
		}
		for (var k : keys) userData.remove(k);
	}

	private UserDataIo() { Common.throwStaticClassInstantiateError(); }
	
}
