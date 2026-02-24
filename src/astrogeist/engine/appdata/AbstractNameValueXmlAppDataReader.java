package astrogeist.engine.appdata;

import java.io.InputStream;
import java.util.Map;

import aha.common.abstraction.io.appdata.AppDataReader;
import aha.common.io.appdata.AbstractAppData;
import astrogeist.engine.util.io.NameValueXml;

public abstract class AbstractNameValueXmlAppDataReader
	extends AbstractAppData implements AppDataReader {
	
	protected AbstractNameValueXmlAppDataReader(Class<?> type) { super(type); }
	
	protected final Map<String, String> load(InputStream is)
		throws Exception { return NameValueXml.INSTANCE.parse(is); }
}
