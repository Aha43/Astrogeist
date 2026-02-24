package astrogeist.engine.util.io;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Element;

import aha.common.io.xml.dom.DomXmlUtil;

/**
 * <p>
 *   Serialize and parse a simple XML dialect named string value pairs.
 * </p>
 */
public class NameValueXml {
	private final String root;
	private final String element;
	private final String name;
	private final String value;
	
	/**
	 * <p>
	 *   Instance of shared
	 *   {@link NameValueXml} where
	 *   {@link #root()} is 'root',
	 *   {@link #element()} is 'e',
	 *   {@link #name()} is 'name' and
	 *   {@link #value()} is 'value'.
	 * </p>
	 */
	public static final NameValueXml INSTANCE = new NameValueXml();
	
	private NameValueXml() { this("data", "e", "key", "value"); }
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param root    {@link #root()}.  
	 * @param element {@link #element()}.
	 * @param name    {@link #name()}.
	 * @param value   {@link #value()}.
	 */
	public NameValueXml(String root, String element, String name, 
		String value) { 
		
		this.root = requireNonEmpty(root, "root");
		this.element = requireNonEmpty(element, "element");
		this.name = requireNonEmpty(name, "name");
		this.value = requireNonEmpty(value, "value");
	}
	
	/**
	 * <p>
	 *   Gets the name of the root element.
	 * </p>
	 * @return the name.
	 */
	public final String root() { return this.root; }
	
	/**
	 * <p>
	 *   Gets the name of the element that have attributes for name and value.
	 * </p>
	 * @return the name.
	 */
	public final String element() { return this.element; }
	
	/**
	 * <p>
	 *   Gets the name of the attribute (of element named
	 *   {@link #element()}) that holds the <i>name</i> a value is indexed by.  
	 * </p>
	 * @return the name.
	 */
	public final String name() { return this.name; }
	
	/**
	 * <p>
	 *   Gets the name of the attribute (of element named
	 *   {@link #element()}) that holds the <i>value</i>.  
	 * </p>
	 * @return the name.
	 */
	public final String value() { return this.value; }
	
	/**
	 * <p>
	 *   Parse.
	 * </p>
	 * @param f the file to parse.
	 * @return a map where keys are the value names. If file does not exist or
	 *         is {@code null} an empty map is returned.
	 * @throws NullPointerException If {@code f} is {@code null}.
	 * @throws Exception If parse fails.
	 */
	public final Map<String, String> parse(File f) throws Exception {
		requireNonNull(f, "f");
		if (!f.exists() || f.length() == 0L) return Map.of();
		try (var is = new FileInputStream(f)){ return parse(is); }
	}
	
	/**
	 * <p>
	 *   Parse.
	 * </p>
	 * @param p the path to the file to parse.
	 * @return a map where keys are the value names. If file does not exist or
	 *         is {@code null} an empty map is returned.
	 * @throws NullPointerException If {@code p} is {@code null}.
	 * @throws Exception If parse fails.
	 */
	public final Map<String, String> parse(Path p) throws Exception {
		requireNonNull(p, "p");
		return parse(p.toFile());
	}
	
	/**
	 * <p>
	 *   Parse.
	 * </p>
	 * @param is the stream to parse. This method does not close the stream.
	 * @return a map where keys are the value names. If file does not exist or
	 *         is {@code null} an empty map is returned.
	 * @throws NullPointerException If {@code is} is {@code null}.
	 * @throws Exception If parse fails.
	 */
	public final Map<String, String> parse(InputStream is) throws Exception {
		requireNonNull(is, "is");
		
		Map<String, String> retVal = new LinkedHashMap<>();

        var doc = DomXmlUtil.parse(is);

        var list = doc.getElementsByTagName(this.element);
        for (var i = 0; i < list.getLength(); i++) {
            var elem = (Element) list.item(i);
            var name = elem.getAttribute(this.name);
            var value = elem.getAttribute(this.value);
            retVal.put(name, value);
        }

        return retVal;
    }
	
	/**
	 * <p>
	 *   Serialize.
	 * </p>
	 * @param data the data to serialize.
	 * @param f    the file to write to.
	 * @throws NullPointerException If {@code data} or {@code f} is
	 *         {@code null}. 
	 * @throws Exception if serialization fails.
	 */
	public final void serialize(Map<String, Object> data, File f) 
		throws Exception {
		
		requireNonNull(data, "data");
		requireNonNull(f, "f");
		
		try (var os = new FileOutputStream(f)) { serialize(data, os); } 
	}
	
	/**
	 * <p>
	 *   Serialize.
	 * </p>
	 * @param data the data to serialize.
	 * @param p    path to the file to write to.
	 * @throws NullPointerException If {@code data} or {@code p} is
	 *         {@code null}. 
	 * @throws Exception if serialization fails.
	 */
	public final void serialize(Map<String, Object> data, Path p)
		throws Exception {
		
		requireNonNull(data, "data");
		requireNonNull(p, "p");
		
		try (var os = Files.newOutputStream(p)){ this.serialize(data, os); }
	}
	
	/**
	 * <p>
	 *   Serialize.
	 * </p>
	 * @param data the data to serialize.
	 * @param os   the stream to write to.
	 * @throws NullPointerException If {@code data} or {@code os} is
	 *         {@code null}. 
	 * @throws Exception if serialization fails.
	 */
	public final void serialize(Map<String, Object> data,
		OutputStream os) throws Exception {
		
		requireNonNull(data, "data");
		requireNonNull(os, "os");
        
		var docAndRoot = DomXmlUtil.newDocumentWithRoot(this.root);
        var doc = docAndRoot.first();
        var rootElement = docAndRoot.second();

        for (var entry : data.entrySet()) {
            var elem = doc.createElement(this.element);
            elem.setAttribute(this.name, entry.getKey());
            elem.setAttribute(this.value, entry.getValue().toString());
            rootElement.appendChild(elem);
        }

        DomXmlUtil.serialize(doc, os);
    }

}
