package astrogeist.engine.userdata;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;

public class UserDataDefinitions {
    private final List<UserDataDefinition> userDataDefinitions = new ArrayList<>();

    public List<UserDataDefinition> getUserDataDefinitions() { return userDataDefinitions; }
    
    public List<String> getUserDataNames(){
    	var names = userDataDefinitions.stream()
    		.map(UserDataDefinition::name)
    		.collect(Collectors.toList());
    	return names;
    }
    
    public static UserDataDefinitions fromXml(Path path) throws Exception {
        var doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path.toFile());
        var defs = new UserDataDefinitions();
        
        var nodes = doc.getElementsByTagName("Def");
        for (var i = 0; i < nodes.getLength(); i++) {
            var element = (Element) nodes.item(i);
            var name = element.getAttribute("name");
            var type = element.getAttribute("type");
            if (type == null || type.isBlank()) type = "String";

            List<String> values = new ArrayList<>();
            var valueNodes = element.getElementsByTagName("Value");
            for (var j = 0; j < valueNodes.getLength(); j++) {
                values.add(valueNodes.item(j).getTextContent().trim());
            }

            defs.userDataDefinitions.add(new UserDataDefinition(name, type, values));
        }

        return defs;
    }
}
