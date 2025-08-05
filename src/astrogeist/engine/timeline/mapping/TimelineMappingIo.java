package astrogeist.engine.timeline.mapping;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import astrogeist.Common;

public final class TimelineMappingIo {
	public static List<TimelineMappingEntry> load(File file) throws Exception {
        List<TimelineMappingEntry> result = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(file);
        doc.getDocumentElement().normalize();

        NodeList mapNodes = doc.getElementsByTagName("map");

        for (int i = 0; i < mapNodes.getLength(); i++) {
            Element mapEl = (Element) mapNodes.item(i);
            String timeline = mapEl.getAttribute("timeline");

            List<String> aliases = new ArrayList<>();
            NodeList aliasNodes = mapEl.getElementsByTagName("alias");
            for (int j = 0; j < aliasNodes.getLength(); j++) {
                aliases.add(aliasNodes.item(j).getTextContent().trim());
            }

            if (!aliases.isEmpty()) {
                result.add(new TimelineMappingEntry(timeline, aliases));
            }
        }

        return result;
    }
	
	public static void save(File file, List<TimelineMappingEntry> entries) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().newDocument();

        Element root = doc.createElement("timeline-mapping");
        doc.appendChild(root);

        for (TimelineMappingEntry entry : entries) {
            Element mapEl = doc.createElement("map");
            mapEl.setAttribute("timeline", entry.timelineField());
            root.appendChild(mapEl);

            for (String alias : entry.aliases()) {
                Element aliasEl = doc.createElement("alias");
                aliasEl.setTextContent(alias);
                mapEl.appendChild(aliasEl);
            }
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        transformer.transform(new DOMSource(doc), new StreamResult(file));
    }
	
	private TimelineMappingIo() { Common.throwStaticClassInstantiateError(); }
}
