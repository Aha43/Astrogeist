package astrogeist.engine.util.io;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
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
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.typesystem.Type;

public final class NameValueMapXml {
	public static void save(LinkedHashMap<String, String> data, File file) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Root element
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("data");
        doc.appendChild(rootElement);

        for (Map.Entry<String, String> entry : data.entrySet()) {
            Element setting = doc.createElement("e");
            setting.setAttribute("key", entry.getKey());
            setting.setAttribute("value", entry.getValue());
            rootElement.appendChild(setting);
        }

        // Write to file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Pretty print
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }
	
	public static void saveTimelineValues(LinkedHashMap<String, TimelineValue> data, File file) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Root element
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("data");
        doc.appendChild(rootElement);

        for (Map.Entry<String, TimelineValue> entry : data.entrySet()) {
            Element setting = doc.createElement("e");
            
            setting.setAttribute("key", entry.getKey());
            
            var tlv = entry.getValue();
            var v = tlv.value();
            setting.setAttribute("value", v);
            
            rootElement.appendChild(setting);
        }

        // Write to file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Pretty print
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }
	
	public static LinkedHashMap<String, String> load(File file) throws Exception {
        LinkedHashMap<String, String> retVal = new LinkedHashMap<>();
        
        if (file.length() == 0L) return retVal;

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(file);
        doc.getDocumentElement().normalize();

        NodeList list = doc.getElementsByTagName("e");
        for (int i = 0; i < list.getLength(); i++) {
            Element elem = (Element) list.item(i);
            String key = elem.getAttribute("key");
            String value = elem.getAttribute("value");
            retVal.put(key, value);
        }

        return retVal;
    }
	
	public static LinkedHashMap<String, TimelineValue> loadTimeKineValues(File file) throws Exception {
		LinkedHashMap<String, TimelineValue> retVal = new LinkedHashMap<>();
        
        if (file.length() == 0L) return retVal;

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(file);
        doc.getDocumentElement().normalize();

        NodeList list = doc.getElementsByTagName("e");
        for (int i = 0; i < list.getLength(); i++) {
            Element elem = (Element) list.item(i);
            String key = elem.getAttribute("key");
            String value = elem.getAttribute("value");
            retVal.put(key, new TimelineValue(value, Type.Text()));
        }

        return retVal;
	}

	private NameValueMapXml() { Common.throwStaticClassInstantiateError(); }
}
