package astrogeist.ui.swing.tool.sun.sketching;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SunSketchXmlMapper {
    private SunSketchXmlMapper() {}

    // ---------- Serialization ----------
    public static Document toDocument(SunSketchDbo d) throws Exception {
        DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = b.newDocument();

        Element root = doc.createElement("sunSketch");
        root.setAttribute("schema", d.schema);
        root.setAttribute("id", d.id);
        root.setAttribute("createdUtc", d.createdUtc.toString());
        root.setAttribute("modifiedUtc", d.modifiedUtc.toString());
        doc.appendChild(root);

        // <canvas>
        Element canvas = doc.createElement("canvas");
        canvas.setAttribute("widthPx", Integer.toString(d.canvas.widthPx));
        canvas.setAttribute("heightPx", Integer.toString(d.canvas.heightPx));
        root.appendChild(canvas);

        // <sunStyle>
        Element sunStyle = doc.createElement("sunStyle");
        sunStyle.setAttribute("paddingFraction", Double.toString(d.sunStyle.paddingFraction));
        root.appendChild(sunStyle);

        // <background>
        Element bg = doc.createElement("background");
        bg.setAttribute("color", toHex(d.sunStyle.background));
        sunStyle.appendChild(bg);

        // <disk>
        Element disk = doc.createElement("disk");
        disk.setAttribute("color", toHex(d.sunStyle.disk.color));
        sunStyle.appendChild(disk);
        Element diskLut = doc.createElement("lut");
        diskLut.setAttribute("name", nullToEmpty(d.sunStyle.disk.lut.name));
        diskLut.setAttribute("t", Double.toString(d.sunStyle.disk.lut.t));
        disk.appendChild(diskLut);

        // <limb>
        Element limb = doc.createElement("limb");
        limb.setAttribute("color", toHex(d.sunStyle.limb.color));
        limb.setAttribute("strokePx", Integer.toString(d.sunStyle.limb.strokePx));
        sunStyle.appendChild(limb);
        Element limbLut = doc.createElement("lut");
        limbLut.setAttribute("name", nullToEmpty(d.sunStyle.limb.lut.name));
        limbLut.setAttribute("t", Double.toString(d.sunStyle.limb.lut.t));
        limb.appendChild(limbLut);

        // <features> (lists empty for now)
        Element features = doc.createElement("features");
        features.appendChild(doc.createElement("prominences"));
        features.appendChild(doc.createElement("sunspots"));
        features.appendChild(doc.createElement("filaments"));
        root.appendChild(features);

        return doc;
    }

    public static void save(Document doc, Path file) throws Exception {
        Files.createDirectories(file.getParent());
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        t.transform(new DOMSource(doc), new StreamResult(Files.newOutputStream(file)));
    }

    // ---------- Utilities ----------
    private static String toHex(Color c) {
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }
    private static String nullToEmpty(String s) { return s == null ? "" : s; }
}

