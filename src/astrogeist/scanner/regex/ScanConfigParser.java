package astrogeist.scanner.regex;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ScanConfigParser {

    public static ScanConfig parse(File xmlFile) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        var config = new ScanConfig();

        // Parse timestampResolvers
        NodeList tsNodes = doc.getElementsByTagName("timestamp");
        for (int i = 0; i < tsNodes.getLength(); i++) {
            Element el = (Element) tsNodes.item(i);
            config.timestampResolvers.add(new TimestampConfig(
                el.getElementsByTagName("regex").item(0).getTextContent(),
                el.getElementsByTagName("format").item(0).getTextContent(),
                el.getElementsByTagName("timezone").item(0).getTextContent()
            ));
        }

        // Parse subjectResolvers
        NodeList subNodes = doc.getElementsByTagName("subject");
        for (int i = 0; i < subNodes.getLength(); i++) {
            Element el = (Element) subNodes.item(i);
            config.subjectResolvers.add(el.getElementsByTagName("regex").item(0).getTextContent());
        }

        // Parse softwareResolvers
        NodeList swNodes = doc.getElementsByTagName("software");
        for (int i = 0; i < swNodes.getLength(); i++) {
            Element el = (Element) swNodes.item(i);
            String regex = el.getElementsByTagName("regex").item(0).getTextContent();

            Map<String, String> mapping = new HashMap<>();
            NodeList entries = el.getElementsByTagName("entry");
            for (int j = 0; j < entries.getLength(); j++) {
                Element entry = (Element) entries.item(j);
                mapping.put(entry.getAttribute("from"), entry.getAttribute("to"));
            }

            config.softwareResolvers.add(new SoftwareConfig(regex, mapping));
        }

        return config;
    }

    public static class ScanConfig {
        public List<TimestampConfig> timestampResolvers = new ArrayList<>();
        public List<String> subjectResolvers = new ArrayList<>();
        public List<SoftwareConfig> softwareResolvers = new ArrayList<>();
    }

    public static class TimestampConfig {
        public final String regex, format, timezone;
        public TimestampConfig(String r, String f, String tz) {
            this.regex = r; this.format = f; this.timezone = tz;
        }
    }

    public static class SoftwareConfig {
        public final String regex;
        public final Map<String, String> mapping;
        public SoftwareConfig(String regex, Map<String, String> mapping) {
            this.regex = regex;
            this.mapping = mapping;
        }
    }
    
}

