package astrogeist.userdata;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;

public class UserDataDefinitions {
    private final List<UserDataDefinition> properties = new ArrayList<>();

    public List<UserDataDefinition> getProperties() { return properties; }
    
    public static UserDataDefinitions fromXml(Path path) throws Exception {
        var doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path.toFile());
        var sidecar = new UserDataDefinitions();
        
        var nodes = doc.getElementsByTagName("Def");
        for (int i = 0; i < nodes.getLength(); i++) {
            var element = (Element) nodes.item(i);
            String name = element.getAttribute("name");
            String type = element.getAttribute("type");
            if (type == null || type.isBlank()) type = "String";

            List<String> values = new ArrayList<>();
            var valueNodes = element.getElementsByTagName("Value");
            for (int j = 0; j < valueNodes.getLength(); j++) {
                values.add(valueNodes.item(j).getTextContent().trim());
            }

            sidecar.properties.add(new UserDataDefinition(name, type, values));
        }

        return sidecar;
    }
}
