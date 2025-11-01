package astrogeist.engine.util.io;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import aha.common.Guards;

public final class XmlUtil {
	private XmlUtil() { Guards.throwStaticClassInstantiateError(); }
	
	public static final DocumentBuilder newDocumentBuilder() throws Exception {
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

        // Harden the parser (XXE-safe)
        f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        f.setFeature("http://xml.org/sax/features/external-general-entities", false);
        f.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        f.setXIncludeAware(false);
        f.setExpandEntityReferences(false);

        var retVal = f.newDocumentBuilder();
        return retVal;
	}
	
}
