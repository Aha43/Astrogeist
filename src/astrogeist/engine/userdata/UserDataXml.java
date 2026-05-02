package astrogeist.engine.userdata;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;
import static aha.common.io.xml.dom.DomXmlUtil.newDocumentWithRoot;
import static aha.common.util.Strings.isNullOrBlank;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import aha.common.io.xml.dom.DomXmlUtil;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.timeline.TimelineValue;

/**
 * <p>
 *   Static methods of use when parsing or generating XML of user timeline data.
 * </p>
 */
public final class UserDataXml {
	private UserDataXml() { throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Parse a xml file containing user entered data for an observation.
	 * </p>
	 * @param pool {@link TimelineValuePool} to fetch / create
	 *             {@link TimelineValue} objects in from parsed data.
	 * @param file the file to parse.
	 * @return a map where keys are name of the value and values are the 
	 *         {@link TimelineValue} objects associated with name.
	 * @throws NullPointerException if either {@code pool} or {@code f} is
	 *         {@code null}.
	 * @throws Exception if parse fails.
	 */
	public final static Map<String, TimelineValue> parse(
		TimelineValuePool pool, File f) throws Exception {
		
		requireNonNull(pool, "pool");
		requireNonNull(f, "f");
		
		if (f.length() == 0) return new LinkedHashMap<>();
		
		try (var is = new FileInputStream(f)) { return parse(pool, is); }
	}
	
	/**
	 * <p>
	 *   Parse xml containing user entered data for an observation.
	 * </p>
	 * @param pool {@link TimelineValuePool} to fetch / create
	 *             {@link TimelineValue} objects in from parsed data.
	 * @param is   the stream to parse.
	 * @return a map where keys are name of the value and values are the 
	 *         {@link TimelineValue} objects associated with name.
	 * @throws NullPointerException if either {@code pool} or {@code f} is
	 *         {@code null}.
	 * @throws Exception if parse fails.
	 */
	public final static Map<String, TimelineValue> parse(
		TimelineValuePool pool, InputStream is) throws Exception {
		
		requireNonNull(pool, "pool");
		requireNonNull(is, "is");
		
		LinkedHashMap<String, TimelineValue> retVal = new LinkedHashMap<>();

	    var doc = DomXmlUtil.parse(is);

	    var list = doc.getElementsByTagName("e");
	    for (int i = 0; i < list.getLength(); i++) {
	    	var elem = (Element) list.item(i);
	    	
	        var key = elem.getAttribute("key");
	        var value = elem.getAttribute("value");
	        var type = elem.getAttribute("type");
	        var tlv = isNullOrBlank(type) ? 
	        	pool.get(key, value) : pool.get(type, value);
	        retVal.put(key, tlv);
	    }

	    return retVal;
	}
	
	/**
	 * <p>
	 *   Serialize data entered to by user for an observation to given file.
	 * </p>
	 * @param data the data to save, a map where keys are name of the value and 
	 *             values are the 
	 *             {@link TimelineValue} objects associated with name.
	 * @param f    the file to save to.
	 * @throws Exception if saving to file fails.
	 */
	public final static void serialize(Map<String, TimelineValue> data, File f)
		throws Exception {
		
		requireNonNull(data, "data");
		requireNonNull(f, "f");
		
		try (var os = new FileOutputStream(f)) { serialize(data, os); } 
	}
	
	/**
	 * <p>
	 *   Serialize data entered to by user for an observation to given.
	 * </p>
	 * @param data the data to save, a map where keys are name of the value and 
	 *             values are the 
	 *             {@link TimelineValue} objects associated with name.
	 * @param os   the stream to save to.
	 * @throws Exception if saving to file fails.
	 */
	public final static void serialize(Map<String, TimelineValue> data,
		OutputStream os) throws Exception {
		
		requireNonNull(data, "data");
		requireNonNull(os, "os");
		
		var doc = createDocument(data);
		DomXmlUtil.serialize(doc, os);
	}
	
	private final static Document createDocument(
		Map<String, TimelineValue> data) throws Exception {
		
		var docAndRoot = newDocumentWithRoot("data");
	    var doc = docAndRoot.first();
	    var rootElement = docAndRoot.second();
	        
	    for (Map.Entry<String, TimelineValue> entry : data.entrySet()) {
	    	var setting = doc.createElement("e");
	            
	        setting.setAttribute("key", entry.getKey());
	            
	        var tlv = entry.getValue();
	            
	        var v = tlv.value();
	        setting.setAttribute("value", v);
	            
	        var type = tlv.type();
	        if (type != null) setting.setAttribute("type", type.getName());
	            
	        rootElement.appendChild(setting);
	    }
	    
	    return doc;
	}
	
}
