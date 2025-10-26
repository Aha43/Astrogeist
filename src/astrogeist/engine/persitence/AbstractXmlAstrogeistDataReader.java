package astrogeist.engine.persitence;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import astrogeist.engine.abstraction.persistence.AstrogeistDataReader;

public abstract class AbstractXmlAstrogeistDataReader implements AstrogeistDataReader  {
	
	private final Class<?> type;
	
	@Override public final Class<?> type() { return this.type; }
	@Override public final String format() { return "xml"; }
	
	protected AbstractXmlAstrogeistDataReader(Class<?> type) { this.type = type; }
	
	protected final Document parse(InputStream is) throws Exception {
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

        // Harden the parser (XXE-safe)
        f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        f.setFeature("http://xml.org/sax/features/external-general-entities", false);
        f.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        f.setXIncludeAware(false);
        f.setExpandEntityReferences(false);

        var builder = f.newDocumentBuilder();
        
        var retVal = builder.parse(is);
        return retVal;
	}

}
