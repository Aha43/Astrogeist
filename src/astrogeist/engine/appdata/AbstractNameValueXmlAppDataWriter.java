package astrogeist.engine.appdata;

import java.io.OutputStream;
import java.util.Map;

import aha.common.abstraction.io.appdata.AppDataWriter;
import aha.common.io.appdata.AbstractAppData;
import astrogeist.engine.util.io.NameValueMapXml;

public abstract class AbstractNameValueXmlAppDataWriter extends AbstractAppData
	implements AppDataWriter {
	
	protected AbstractNameValueXmlAppDataWriter(Class<?> type) { super(type); }
	
	protected final void write(OutputStream out, Map<String, Object> data)
		throws Exception {
		
		NameValueMapXml.save(data, out); 
	}
}
