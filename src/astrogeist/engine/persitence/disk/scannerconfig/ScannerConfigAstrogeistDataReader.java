package astrogeist.engine.persitence.disk.scannerconfig;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import astrogeist.engine.abstraction.persistence.AstrogeistDataReader;

public final class ScannerConfigAstrogeistDataReader implements AstrogeistDataReader {

	@Override public final Class<?> type() { return ScanningConfiguration.class; }
	
	@Override public final String format() { return "xml"; }
	
	@Override public Object createDefault() { return new ScanningConfiguration(); }
		
	@Override public Object read(InputStream in) throws Exception {
		var data = parse(in);
		var retVal = new ScanningConfiguration(data);
		return retVal;
	}
	
	private static Map<String, List<String>> parse(InputStream in) throws Exception {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

        // Harden the parser (XXE-safe)
        f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        f.setFeature("http://xml.org/sax/features/external-general-entities", false);
        f.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        f.setXIncludeAware(false);
        f.setExpandEntityReferences(false);

        var b = f.newDocumentBuilder();
        var doc = b.parse(in);
        doc.getDocumentElement().normalize();

        Map<String, List<String>> map = new LinkedHashMap<>(); // preserve order

        NodeList scannerNodes = doc.getElementsByTagName("scanner");
        for (int i = 0; i < scannerNodes.getLength(); i++) {
            var node = scannerNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;

            var scannerEl = (Element) node;
            var type = scannerEl.getAttribute("type");
            if (type == null || type.isBlank()) continue; // skip invalid entries

            List<String> locations = new ArrayList<>();
            // collect immediate <location> children (preserve order)
            var children = scannerEl.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                var c = children.item(j);
                if (c.getNodeType() == Node.ELEMENT_NODE && "location".equals(c.getNodeName())) {
                    var value = c.getTextContent();
                    if (value != null) {
                        var trimmed = value.trim();
                        if (!trimmed.isEmpty()) locations.add(trimmed);
                    }
                }
            }

            map.put(type, locations);
        }

        return map;
    }

}
