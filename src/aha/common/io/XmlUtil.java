package aha.common.io;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * <p>
 *   Utility methods of use when parsing XML.
 * </p> 
 */
public final class XmlUtil {
	private XmlUtil() { throwStaticClassInstantiateError(); }
	
	public static final DocumentBuilder newDocumentBuilder() throws Exception {
		var f = DocumentBuilderFactory.newInstance();

        // Harden the parser (XXE-safe)
        f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", 
        	true);
        f.setFeature("http://xml.org/sax/features/external-general-entities",
        	false);
        f.setFeature("http://xml.org/sax/features/external-parameter-entities", 
        	false);
        f.setXIncludeAware(false);
        f.setExpandEntityReferences(false);

        var retVal = f.newDocumentBuilder();
        return retVal;
	}
	
	public static final Document parse(File file) throws Exception {
		return parse(new FileInputStream(file)); }
	
	public static final Document parse(InputStream is) throws Exception {
      var builder = XmlUtil.newDocumentBuilder();
      var retVal = builder.parse(is);
      return retVal;
	}
}
