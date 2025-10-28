package astrogeist.engine.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import astrogeist.common.Guards;
import astrogeist.common.tuple.Tuple2;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.timeline.TimelineValue;

public final class NameValueMapXml {
	private NameValueMapXml() { Guards.throwStaticClassInstantiateError(); }
	
	private final static Tuple2<Document, Element> newDocumentWithRoot() throws Exception {
		var docBuilder = XmlUtil.newDocumentBuilder();
		var doc = docBuilder.newDocument();
        var rootElement = doc.createElement("data");
        doc.appendChild(rootElement);
        var retVal = Tuple2.of(doc, rootElement);
        return retVal;
	}
	
	private final static void serialize(Document doc, OutputStream os) throws Exception {
		var transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Pretty print
        var source = new DOMSource(doc);
        var result = new StreamResult(os);
        transformer.transform(source, result);
	}
	
	public final static void save(LinkedHashMap<String, String> data, File file) throws Exception {
		try (var os = new FileOutputStream(file)) { save(data, os); } }
	
	public final static void save(LinkedHashMap<String, String> data, OutputStream os) throws Exception {
        var docAndRoot = newDocumentWithRoot();
        var doc = docAndRoot.first();
        var rootElement = docAndRoot.second();

        for (var entry : data.entrySet()) {
            Element setting = doc.createElement("e");
            setting.setAttribute("key", entry.getKey());
            setting.setAttribute("value", entry.getValue());
            rootElement.appendChild(setting);
        }

        serialize(doc, os);
    }
	
	public final static void saveTimelineValues(LinkedHashMap<String, TimelineValue> data, File file) throws Exception {
		try (var os = new FileOutputStream(file)) { saveTimelineValues(data, os); } }
	
	public final static void saveTimelineValues(LinkedHashMap<String, TimelineValue> data, OutputStream os) throws Exception {
		var docAndRoot = newDocumentWithRoot();
        var doc = docAndRoot.first();
        var rootElement = docAndRoot.second();
        
        for (Map.Entry<String, TimelineValue> entry : data.entrySet()) {
        	var setting = doc.createElement("e");
            
            setting.setAttribute("key", entry.getKey());
            
            var tlv = entry.getValue();
            var v = tlv.value();
            setting.setAttribute("value", v);
            
            rootElement.appendChild(setting);
        }

        serialize(doc, os);
    }
	
	private final static Document parse(InputStream is) throws Exception {
		var docBuilder = XmlUtil.newDocumentBuilder();
        var doc = docBuilder.parse(is);
        doc.getDocumentElement().normalize();
        return doc;
	}
	
	public final static LinkedHashMap<String, String> load(File file) throws Exception {
		if (file.length() == 0L) return new LinkedHashMap<String, String>();
		try (var is = new FileInputStream(file)){ return load(is); }
	}
	
	public final static LinkedHashMap<String, String> load(InputStream is) throws Exception {
        LinkedHashMap<String, String> retVal = new LinkedHashMap<>();

        var doc = parse(is);

        var list = doc.getElementsByTagName("e");
        for (var i = 0; i < list.getLength(); i++) {
            Element elem = (Element) list.item(i);
            String key = elem.getAttribute("key");
            String value = elem.getAttribute("value");
            retVal.put(key, value);
        }

        return retVal;
    }
	
	public final static LinkedHashMap<String, TimelineValue> loadTimeLineValues(
		TimelineValuePool pool, File file) throws Exception {
		
		if (file.length() == 0L) return new LinkedHashMap<String, TimelineValue>();
		try (var is = new FileInputStream(file)) { return loadTimeLineValues(pool, is); }
	}
	
	public final static LinkedHashMap<String, TimelineValue> loadTimeLineValues(
		TimelineValuePool pool, InputStream is) throws Exception {
		
		LinkedHashMap<String, TimelineValue> retVal = new LinkedHashMap<>();

        var doc = parse(is);

        var list = doc.getElementsByTagName("e");
        for (int i = 0; i < list.getLength(); i++) {
        	var elem = (Element) list.item(i);
        	var key = elem.getAttribute("key");
        	var value = elem.getAttribute("value");
            var tlv = pool.get(key, value);
            retVal.put(key, tlv);
        }

        return retVal;
	}

}
