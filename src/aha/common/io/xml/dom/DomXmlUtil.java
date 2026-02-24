package aha.common.io.xml.dom;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;
import static aha.common.guard.StringGuards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import aha.common.tuple.Tuple2;

/**
 * <p>
 *   Utility methods for secure and portable XML parsing and serialization
 *   based on the standard JAXP DOM APIs.
 * </p>
 * 
 * <h2>Design Philosophy</h2>
 *
 * <p>
 *   This utility is <i>secure by default</i>. All factories created by this
 *   class are configured to mitigate XML External Entity (XXE) attacks and
 *   external resource resolution vulnerabilities by:
 * </p>
 * <ul>
 *   <li>
 *     Enabling {@link javax.xml.XMLConstants#FEATURE_SECURE_PROCESSING}.
 *   </li>
 *   <li>
 *     Disabling DOCTYPE declarations and external entity resolution.
 *   </li>
 *   <li>
 *     Blocking access to external DTDs, schemas, and stylesheets.
 *   </li>
 * </ul>
 *
 * <p>
 *   The intent is that XML originating from untrusted or semi-trusted sources
 *   can be parsed without additional configuration by callers.
 * </p>
 *
 * <h2>Portability</h2>
 *
 * <p>
 *   Only standard JAXP features and properties are required. Where
 *   vendor-specific properties (such as indentation width) are used,
 *   they are applied defensively and ignored if unsupported in the
 *   current runtime environment.
 * </p>
 *
 * <h2>Thread Safety</h2>
 *
 * <p>
 *   {@link javax.xml.parsers.DocumentBuilder} and
 *   {@link javax.xml.transform.Transformer} instances are not specified
 *   to be thread-safe. Callers should create a new instance per operation
 *   or otherwise ensure external synchronization.
 * </p>
 *
 * <h2>Scope</h2>
 *
 * <p>
 *   This class intentionally provides only DOM-based convenience methods.
 *   It is suitable for configuration files, structured application data,
 *   and other moderate-sized XML documents. It is not intended for
 *   streaming large XML payloads; for such use cases, a StAX- or SAX-based
 *   utility should be used instead.
 * </p>
 *
 * <p>
 *   The class is a static utility and cannot be instantiated.
 * </p>
 */
public final class DomXmlUtil {
	private DomXmlUtil() { throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Creates a {@link DocumentBuilder} configured for secure and portable
	 *   XML parsing.
	 * </p>
	 * <p>
	 *   The underlying {@link DocumentBuilderFactory} is hardened to mitigate
	 *   XML External Entity (XXE) attacks and related external resource
	 *   resolution vulnerabilities.
	 * </p>
	 * <p>
	 *   The factory is configured as follows:
	 * </p>
	 * <ul>
	 *   <li>
	 *     Enables {@link XMLConstants#FEATURE_SECURE_PROCESSING}.
	 *   </li>
	 *   <li>
	 *     Disallows DOCTYPE declarations via
	 *     {@code "http://apache.org/xml/features/disallow-doctype-decl"}.
	 *   </li>
	 *   <li>
	 *     Disables external general entities via
	 *     {@code "http://xml.org/sax/features/external-general-entities"}.
	 *   </li>
	 *   <li>
	 *     Disables external parameter entities via
	 *     {@code "http://xml.org/sax/features/external-parameter-entities"}.
	 *   </li>
	 *   <li>
	 *     Disables XInclude processing.
	 *   </li>
	 *   <li>
	 *     Disables automatic expansion of entity references.
	 *   </li>
	 *   <li>
	 *     Blocks access to external DTDs and schemas by setting
	 *     {@link XMLConstants#ACCESS_EXTERNAL_DTD} and
	 *     {@link XMLConstants#ACCESS_EXTERNAL_SCHEMA} to the empty string.
	 *   </li>
	 * </ul>
	 * <p>
	 *   These settings make the returned
	 *   {@link DocumentBuilder} suitable for
	 *   parsing untrusted or semi-trusted XML input in typical application
	 *   scenarios.
	 * </p>
	 * <p>
	 *   Note that {@link DocumentBuilder} instances are not specified to be
	 *   thread-safe. Callers should create a new instance per parsing operation
	 *   (or otherwise synchronize access).
	 * </p>
	 * @return the configured {@link DocumentBuilder}.
	 * @throws Exception if the builder could not be created or configured.
	 */
	public static final DocumentBuilder newDocumentBuilder() throws Exception {
	    var f = DocumentBuilderFactory.newInstance();
	    f.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

	    // Harden the parser (XXE-safe)
	    f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", 
	    	true);
	    f.setFeature("http://xml.org/sax/features/external-general-entities",
	    	false);
	    f.setFeature("http://xml.org/sax/features/external-parameter-entities",
	    	false);
	    f.setXIncludeAware(false);
	    f.setExpandEntityReferences(false);

	    // JAXP properties to block external resolution:
	    f.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
	    f.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

	    var retVal = f.newDocumentBuilder();
	    return retVal;
	}
	
	/**
	 * <p>
	 *   Convenient method to create a
	 *   {@link Document} with a root element.
	 * </p>
	 * <p>
	 *   Uses the
	 *   {@link DocumentBuilder} provided by
	 *   {@link #newDocumentBuilder()}.
	 * </p>
	 * @param root Name on the root
	 *             {@link Element}.
	 * @return a tuple where first element is the created
	 *         {@link Document} and the second element is the root
	 *         {@link Element}.
	 * @throws Exception If fails for any reason.
	 */
	public static final Tuple2<Document, Element> newDocumentWithRoot(
		String root) throws Exception {
		
		requireNonEmpty(root, "root");
				
		var docBuilder = newDocumentBuilder();
		var doc = docBuilder.newDocument();
		var rootElement = doc.createElement(root);
		doc.appendChild(rootElement);
		var retVal = Tuple2.of(doc, rootElement);
		return retVal;
	}
	
	/**
	 * <p>
	 *   Parse XML file.
	 * </p>
	 * <p>
	 *   Uses the 
	 *   {@link DocumentBuilder} that
	 *   {@link #newDocumentBuilder()} provides.
	 * </p>
	 * <p>
	 *   The XML 
	 *   {@link Document} returned tree is <i>normalized</i> by that
	 *   {@link Element#normalize()} is invoked on its 
	 *   {@link Document#getDocumentElement() document element}.
	 * </p>
	 * @param p the path to the file to parse.
	 * @return {@link Document} that is the result of the parsing of
	 *         {@code file}.
	 * @throws NullPointerException if {@code f} is {@code null}.
	 * @throws Exception if the parse fails.
	 */
	public static final Document parse(Path p) throws Exception {
		requireNonNull(p, "p");
		try (var is = Files.newInputStream(p)) { return parse(is); }
	}
	
	/**
	 * <p>
	 *   Parse XML file.
	 * </p>
	 * <p>
	 *   Uses the 
	 *   {@link DocumentBuilder} that
	 *   {@link #newDocumentBuilder()} provides.
	 * </p>
	 * <p>
	 *   The XML 
	 *   {@link Document} returned tree is <i>normalized</i> by that
	 *   {@link Element#normalize()} is invoked on its 
	 *   {@link Document#getDocumentElement() document element}.
	 * </p>
	 * @param f the file to parse.
	 * @return {@link Document} that is the result of the parsing of
	 *         {@code file}.
	 * @throws NullPointerException if {@code f} is {@code null}.
	 * @throws Exception if the parse fails.
	 */
	public static final Document parse(File f) throws Exception {
		requireNonNull(f, "f");
		try (var is = new FileInputStream(f)) { return parse(is); }
	}
	
	/**
	 * <p>
	 *   Parse XML stream.
	 * </p>
	 * <p>
	 *   Uses the 
	 *   {@link DocumentBuilder} that
	 *   {@link #newDocumentBuilder()} provides.
	 * </p>
	 * <p>
	 *   The XML 
	 *   {@link Document} returned tree is <i>normalized</i> by that
	 *   {@link Element#normalize()} is invoked on its 
	 *   {@link Document#getDocumentElement() document element}.
	 * </p>
	 * @param is the stream to parse. This method does not close the stream.
	 * @return {@link Document} that is the result of the parsing of
	 *         {@code is}.
	 * @throws NullPointerException if {@code is} is {@code null}.
	 * @throws Exception if the parse fails.
	 */
	public static final Document parse(InputStream is) throws Exception  {
		var builder = newDocumentBuilder();
		var retVal = builder.parse(requireNonNull(is, "is"));
		var root = retVal.getDocumentElement();
		if (root != null) root.normalize();
		return retVal;
	}
	
	/**
	 * <p>
	 *   Creates a {@link Transformer} suitable for serializing DOM
	 *   {@link org.w3c.dom.Document}s to XML with stable, human-readable output.
	 * </p>
	 * <p>
	 *   The underlying {@link TransformerFactory} is configured to be
	 *   <i>secure by default</i>:
	 * </p>
	 * <ul>
	 *   <li>
	 *     Enables {@link XMLConstants#FEATURE_SECURE_PROCESSING}.
	 *   </li>
	 *   <li>
	 *     Disallows resolving external DTDs and stylesheets by setting
	 *     {@link XMLConstants#ACCESS_EXTERNAL_DTD} and
	 *     {@link XMLConstants#ACCESS_EXTERNAL_STYLESHEET} to the empty string.
	 *   </li>
	 * </ul>
	 * <p>
	 *   The returned {@link Transformer} is configured with the following
	 *   output properties:
	 * </p>
	 * <ul>
	 *   <li>
	 *     {@link OutputKeys#INDENT} = {@code "yes"}.
	 *   </li>
	 *   <li>
	 *     Xalan indentation amount = {@code "2"} using the vendor property
	 *     {@code "{http://xml.apache.org/xslt}indent-amount"}.
	 *   </li>
	 *   <li>
	 *     {@link OutputKeys#OMIT_XML_DECLARATION} = {@code "no"}.
	 *   </li>
	 *   <li>
	 *     {@link OutputKeys#ENCODING} = {@code "UTF-8"}.
	 *   </li>
	 * </ul>
	 * <p>
	 *   Additionally, the method <i>attempts</i> to set a vendor-specific
	 *   indentation amount property
	 *   ({@code "{http://xml.apache.org/xslt}indent-amount"}).
	 *   This property is not part of the JAXP standard and may be unsupported
	 *   in some environments. If unsupported, it is silently ignored to
	 *   preserve portability.
	 * </p> 
	 * <p>
	 *   Note that a 
	 *   {@link Transformer} is not specified to be thread-safe; callers should
	 *   create a new instance per serialization (or otherwise synchronize
	 *   access).
	 * </p>
	 * @return the created {@link Transformer}.
	 * @throws Exception if the transformer could not be created or configured.
	 */
	public static final Transformer newTransformer() throws Exception {
		var tf = TransformerFactory.newInstance();

		// Harden transformer (external resource access)
		tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

		var retVal = tf.newTransformer();
		retVal.setOutputProperty(OutputKeys.INDENT, "yes"); // Pretty print
		retVal.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
			"2");
		retVal.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		retVal.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		// Optional / vendor-specific indentation amount (portable: ignore if
		// unsupported)
		try {
			retVal.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "2");
		} catch (IllegalArgumentException ignore) {
			// Intentionally ignored: not supported by all Transformer
			// implementations.
		}
		  
		return retVal;
	}
	
	/**
	 * <p>
	 *   Serialize given
	 *   {@link Document} to XML.
	 * </p>
	 * @param doc {@link Document} to serialize.
	 * @param p   the path to file to serialize to.
	 * @throws NullPointerException if either {@code doc} or {@code file} is
	 *         {@code null}.
	 * @throws Exception If serialization fails.
	 */
	public static final void serialize(Document doc, Path p) throws Exception {
		requireNonNull(doc, "doc");
		requireNonNull(p, "p");
		try (var os = Files.newOutputStream(p)) { serialize(doc, os); }
	}
	
	/**
	 * <p>
	 *   Serialize given
	 *   {@link Document} to XML.
	 * </p>
	 * @param doc {@link Document} to serialize.
	 * @param f   the file to serialize to.
	 * @throws NullPointerException if either {@code doc} or {@code file} is
	 *         {@code null}.
	 * @throws Exception If serialization fails.
	 */
	public static final void serialize(Document doc, File f) throws Exception {
		requireNonNull(doc, "doc");
		requireNonNull(f, "f");
		try (var os = new FileOutputStream(f)) { serialize(doc, os); }
	}
	
	/**
	 * <p>
	 *   Serialize given
	 *   {@link Document} to XML.
	 * </p>
	 * @param doc {@link Document} to serialize.
	 * @param os  the stream to serialize to.
	 * @throws NullPointerException if either {@code doc} or {@code os} is
	 *         {@code null}.
	 * @throws Exception If serialization fails.
	 */
	public static final void serialize(Document doc, OutputStream os) 
		throws Exception {
		
		requireNonNull(doc, "doc");
		requireNonNull(os, "os");
		
        var source = new DOMSource(doc);
        var result = new StreamResult(os);
        
        var transformer = newTransformer();
        transformer.transform(source, result);
	}
	
}
