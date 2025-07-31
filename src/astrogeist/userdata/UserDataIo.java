package astrogeist.userdata;

import java.time.Instant;
import java.util.Map;

import astrogeist.Common;
import astrogeist.app.resources.Resources;
import astrogeist.util.io.NameValueMapXml;

public final class UserDataIo {
	public static Map<String, String> load(Instant t) throws Exception {
		var file = Resources.getUserDataFile(t);
		if (!file.exists()) file.createNewFile();
		var retVal = NameValueMapXml.load(file);
		return retVal;
	}
	
	public static void save(Instant t, Map<String, String> userData) throws Exception {
		var file = Resources.getUserDataFile(t);
		NameValueMapXml.save(userData,file);
	}

	private UserDataIo() { Common.throwStaticClassInstantiateError(); }
	
}
