package astrogeist.userprops;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;

public class UserPropDefs {
    private final List<UserPropDef> properties = new ArrayList<>();

    public static UserPropDefs fromXml(Path path) throws Exception {
        var doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path.toFile());
        var sidecar = new UserPropDefs();
        
        var nodes = doc.getElementsByTagName("UserPropDef");
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

            sidecar.properties.add(new UserPropDef(name, type, values));
        }

        return sidecar;
    }

    public List<UserPropDef> getProperties() { return properties; }
}
