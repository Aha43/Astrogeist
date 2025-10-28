package astrogeist.engine.persitence.userdatadefinitions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import astrogeist.engine.persitence.AbstractXmlAstrogeistDataReader;

public final class UserDataDefinitionsAstrogeistDataReader extends AbstractXmlAstrogeistDataReader {

	public UserDataDefinitionsAstrogeistDataReader() { super(UserDataDefinitions.class); }
	
	@Override public Object createDefault() { return new UserDataDefinitions(); }

	@Override public Object read(InputStream in) throws Exception {
		var doc = super.parse(in);
		
		//var doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path.toFile());
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
