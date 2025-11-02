package astrogeist.engine.appdata;

import java.io.InputStream;

import org.w3c.dom.Document;

import aha.common.abstraction.appdata.AppDataReader;
import astrogeist.engine.util.io.XmlUtil;

public abstract class AbstractXmlAstrogeistDataReader implements AppDataReader  {
	
	private final Class<?> type;
	
	@Override public final Class<?> type() { return this.type; }
	@Override public final String format() { return "xml"; }
	
	protected AbstractXmlAstrogeistDataReader(Class<?> type) { this.type = type; }
	
	protected final Document parse(InputStream is) throws Exception {
        var builder = XmlUtil.newDocumentBuilder();
        var retVal = builder.parse(is);
        return retVal;
	}

}
