package astrogeist.engine.appdata;

import java.io.InputStream;
import java.util.LinkedHashMap;

import aha.common.abstraction.io.appdata.AppDataReader;
import aha.common.io.appdata.AbstractAppData;
import astrogeist.engine.util.io.NameValueMapXml;

public abstract class AbstractNameValueXmlAppDataReader
	extends AbstractAppData implements AppDataReader {
	
	protected AbstractNameValueXmlAppDataReader(Class<?> type) { super(type); }
	
	protected final LinkedHashMap<String, String> load(InputStream is)
		throws Exception { return NameValueMapXml.load(is); }
}
