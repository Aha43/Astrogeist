package astrogeist.engine.persitence;

import java.io.OutputStream;
import java.util.LinkedHashMap;

import astrogeist.engine.abstraction.persistence.AstrogeistDataWriter;
import astrogeist.engine.util.io.NameValueMapXml;

public abstract class AbstractNameValueXmlAstrogeistDataWriter implements AstrogeistDataWriter {
	
	private final Class<?> type;
	
	@Override public final Class<?> type() { return this.type; }
	@Override public final String format() { return "xml"; }
	
	protected AbstractNameValueXmlAstrogeistDataWriter(Class<?> type) { this.type = type; }
	
	protected final void write(OutputStream out, LinkedHashMap<String, String> data) throws Exception {
		NameValueMapXml.save(data, out); }
}
