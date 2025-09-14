package astrogeist.engine.scanner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import astrogeist.Common;
import astrogeist.engine.abstraction.PluginScanner;
import astrogeist.engine.abstraction.Scanner;


/**
 * <p>
 *   Methods read / parse scanning configurations and create 
 *   {@link PluginScanner}s from such configuration.
 * </p>
 */
public final class ScannerConfigLoader {
    private ScannerConfigLoader() { Common.throwStaticClassInstantiateError(); }
    
    public final static Map<String, List<String>> parse(File dir) throws Exception {
    	return parse(dir.toPath()); }

    /** Parse from a file path. */
    public final static Map<String, List<String>> parse(Path path) throws Exception {
        try (InputStream in = java.nio.file.Files.newInputStream(path)) {
            return parse(in);
        }
    }

    /** Parse from a String (handy for tests). */
    public final static Map<String, List<String>> parseFromString(String xml) throws Exception {
        try (InputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
            return parse(in);
        }
    }

    /** Parse from an InputStream. */
    public final static Map<String, List<String>> parse(InputStream in) throws Exception {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

        // Harden the parser (XXE-safe)
        f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        f.setFeature("http://xml.org/sax/features/external-general-entities", false);
        f.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        f.setXIncludeAware(false);
        f.setExpandEntityReferences(false);

        DocumentBuilder b = f.newDocumentBuilder();
        Document doc = b.parse(in);
        doc.getDocumentElement().normalize();

        Map<String, List<String>> map = new LinkedHashMap<>(); // preserve order

        NodeList scannerNodes = doc.getElementsByTagName("scanner");
        for (int i = 0; i < scannerNodes.getLength(); i++) {
            Node node = scannerNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;

            Element scannerEl = (Element) node;
            String type = scannerEl.getAttribute("type");
            if (type == null || type.isBlank()) continue; // skip invalid entries

            List<String> locations = new ArrayList<>();
            // collect immediate <location> children (preserve order)
            NodeList children = scannerEl.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                Node c = children.item(j);
                if (c.getNodeType() == Node.ELEMENT_NODE && "location".equals(c.getNodeName())) {
                    String value = c.getTextContent();
                    if (value != null) {
                        String trimmed = value.trim();
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
     *   Creates
     *   {@link PluginScanner}s given configuration provided by output of methods
     *   {@link #parse(InputStream)},
     *   {@link #parse(Path)}
     *   {@link #parseFromString(String)}.
     * </p> 
     * @param config Configuration.
     * @return {@link PluginScanner}s.
     * @throws Exception If fails to create scanners.
     */
    public final static List<Scanner> buildScanners(Map<String, List<String>> config) throws Exception {
        List<Scanner> scanners = new ArrayList<>();
        for (var entry : config.entrySet()) {
            String type = entry.getKey();
            List<String> locations = entry.getValue();
            var scannersForType = PluginLoader.resolveFactory(type, locations);
            scanners.addAll(scannersForType);
        }
        return scanners;
    }
    
}
