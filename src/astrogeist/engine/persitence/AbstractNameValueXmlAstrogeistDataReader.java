package astrogeist.engine.persitence;

import java.io.InputStream;
import java.util.LinkedHashMap;

import astrogeist.engine.abstraction.persistence.AstrogeistDataReader;
import astrogeist.engine.util.io.NameValueMapXml;

public abstract class AbstractNameValueXmlAstrogeistDataReader implements AstrogeistDataReader {
	
	private final Class<?> type;
	
	@Override public final Class<?> type() { return this.type; }
	@Override public final String format() { return "xml"; }
	
	protected AbstractNameValueXmlAstrogeistDataReader(Class<?> type) { this.type = type; }
	
	protected final LinkedHashMap<String, String> load(InputStream is) throws Exception {
		return NameValueMapXml.load(is); }
}
