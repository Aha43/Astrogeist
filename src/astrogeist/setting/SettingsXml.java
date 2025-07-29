package astrogeist.setting;

import java.io.File;
import java.util.HashMap;
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

public final class SettingsXml {
	public static void saveSettings(Map<String, String> settings, File file) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Root element
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("settings");
        doc.appendChild(rootElement);

        for (Map.Entry<String, String> entry : settings.entrySet()) {
            Element setting = doc.createElement("setting");
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
	
	public static Map<String, String> loadSettings(File file) throws Exception {
        Map<String, String> settings = new HashMap<>();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(file);
        doc.getDocumentElement().normalize();

        NodeList list = doc.getElementsByTagName("setting");
        for (int i = 0; i < list.getLength(); i++) {
            Element elem = (Element) list.item(i);
            String key = elem.getAttribute("key");
            String value = elem.getAttribute("value");
            settings.put(key, value);
        }

        return settings;
    }

	private SettingsXml() { Common.throwStaticClassInstantiateError(); }
}
