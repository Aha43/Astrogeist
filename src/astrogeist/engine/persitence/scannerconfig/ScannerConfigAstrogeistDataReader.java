package astrogeist.engine.persitence.scannerconfig;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import astrogeist.engine.persitence.AbstractXmlAstrogeistDataReader;

public final class ScannerConfigAstrogeistDataReader extends AbstractXmlAstrogeistDataReader {
	
	public ScannerConfigAstrogeistDataReader() { super(ScanningConfiguration.class); } 
	
	@Override public final Object createDefault() { return new ScanningConfiguration(); }
		
	@Override public final Object read(InputStream in) throws Exception {
		var data = readXml(in);
		var retVal = new ScanningConfiguration(data);
		return retVal;
	}
	
	private final Map<String, List<String>> readXml(InputStream in) throws Exception {
        var doc = super.parse(in);
        doc.getDocumentElement().normalize();

        Map<String, List<String>> map = new LinkedHashMap<>(); // preserve order

        NodeList scannerNodes = doc.getElementsByTagName("scanner");
        for (int i = 0; i < scannerNodes.getLength(); i++) {
            var node = scannerNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;

            var scannerEl = (Element) node;
            var type = scannerEl.getAttribute("type");
            if (type == null || type.isBlank()) continue; // skip invalid entries

            var locations = new ArrayList<String>();
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
