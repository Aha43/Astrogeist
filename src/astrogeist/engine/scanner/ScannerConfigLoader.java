package astrogeist.engine.scanner;

// jdoc ai rev

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import astrogeist.common.Common;
import astrogeist.engine.abstraction.Scanner;

/**
 * <p>
 *   Reads/parses plug-in scanning configuration and creates {@link PluginScanner}s
 *   from that configuration.
 * </p>
 */
public final class ScannerConfigLoader {
    private ScannerConfigLoader() { Common.throwStaticClassInstantiateError(); }
    
    /**
     * <p>
     *   Parses the given file.
     * </p>
     * @param file the {@link File} to parse.
     * @return a configuration map as described in
     *         {@link #parse(InputStream)}.
     * @throws Exception If fails for any reason.
     */
    final public static Map<String, List<String>> parse(File file) throws Exception {
    	return parse(file.toPath()); }
    
    /**
     * <p>
     *   Parses the given file.
     * </p>
     * @param path the {@link Path} to the file to parse.
     * @return a configuration map as described in
     *         {@link #parse(InputStream)}.
     * @throws Exception If fails for any reason.
     */
    final public static Map<String, List<String>> parse(Path path) throws Exception {
        try (var in = Files.newInputStream(path)) { return parse(in); } }
    
    /**
     * <p>
     *   Parses configuration from a raw XML string (handy for tests).
     * </p>
     * @param xml the XML to parse.
     * @return a configuration map as described in
     *         {@link #parse(InputStream)}.
     * @throws Exception If fails for any reason.
     */
    final public static Map<String, List<String>> parseFromString(String xml) throws Exception {
        try (var in = new ByteArrayInputStream(xml.getBytes(UTF_8))) {
            return parse(in); } }
    
    /**
     * <p>
     *   Parses configuration from an input stream.
     * </p>
     * @param in the {@link InputStream} to parse.
     * @return a map {@code type -> [locations...]} suitable for
     *         passing to {@link #createScanners(Map)} to create {@link Scanner}
     *         components based on the specifications parsed from the file.
     * @throws Exception If fails for any reason.
     */
    final public static Map<String, List<String>> parse(InputStream in) throws Exception {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

        // Harden the parser (XXE-safe)
        f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        f.setFeature("http://xml.org/sax/features/external-general-entities", false);
        f.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        f.setXIncludeAware(false);
        f.setExpandEntityReferences(false);

        var b = f.newDocumentBuilder();
        var doc = b.parse(in);
        doc.getDocumentElement().normalize();

        Map<String, List<String>> map = new LinkedHashMap<>(); // preserve order

        NodeList scannerNodes = doc.getElementsByTagName("scanner");
        for (int i = 0; i < scannerNodes.getLength(); i++) {
            var node = scannerNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;

            var scannerEl = (Element) node;
            var type = scannerEl.getAttribute("type");
            if (type == null || type.isBlank()) continue; // skip invalid entries

            List<String> locations = new ArrayList<>();
            // collect immediate <location> children (preserve order)
            var children = scannerEl.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                var c = children.item(j);
                if (c.getNodeType() == Node.ELEMENT_NODE && "location".equals(c.getNodeName())) {
                    var value = c.getTextContent();
                    if (value != null) {
                        var trimmed = value.trim();
                        if (!trimmed.isEmpty()) locations.add(trimmed);
                    }
                }
            }

            map.put(type, locations);
        }

        return map;
    }
    
    /**
     * <p>
     *   Creates {@link PluginScanner} instances from the supplied configuration.
     * </p>
     * <p>
     *   The returned list is typed as
     *   {@link Scanner} to allow the caller to add other scanner implementations 
     *   alongside the plug-in scanners.
     * </p>
     * @param config a map as returned by 
     *               {@link #parse(InputStream)} (or its overloads).
     * @return a list of created {@link Scanner} components.
     * @throws Exception If fails to create scanners.
     */
    final public static List<Scanner> createScanners(Map<String, List<String>> config) throws Exception {
        List<Scanner> scanners = new ArrayList<>();
        for (var entry : config.entrySet()) {
            var type = entry.getKey();
            var locations = entry.getValue();
            var scannersForType = ScannerLoader.resolveFactory(type, locations);
            scanners.addAll(scannersForType);
        }
        return scanners;
    }
    
}
