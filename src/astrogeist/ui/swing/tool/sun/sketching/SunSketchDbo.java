package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Document-backed object for astrogeist.sun.sketch/v1 */
public final class SunSketchDbo {
    // Header
    public String schema = "astrogeist.sun.sketch/v1";
    public String id = UUID.randomUUID().toString();
    public Instant createdUtc;
    public Instant modifiedUtc;

    // Canvas & style
    public Canvas canvas = new Canvas();
    public SunStyle sunStyle = new SunStyle();

    // Features (empty for now; you’ll grow these as tools arrive)
    public Features features = new Features();

    // ---------- nested types ----------
    public static final class Canvas {
        public int widthPx;
        public int heightPx;
    }

    public static final class SunStyle {
        public double paddingFraction = 0.18;
        public Color background = Color.WHITE;
        public DiskStyle disk = new DiskStyle();
        public LimbStyle limb = new LimbStyle();
    }

    public static final class DiskStyle {
        public Color color = new Color(255, 220, 120);
        public LutRef lut = new LutRef("Classic Red", 0.75);
    }

    public static final class LimbStyle {
        public Color color = new Color(230, 160, 60);
        public int strokePx = 2;
        public LutRef lut = new LutRef("Classic Red", 0.90);
    }

    public static final class LutRef {
        public String name;
        public double t; // 0..1
        public LutRef() {}
        public LutRef(String name, double t) { this.name = name; this.t = t; }
    }

    public static final class Features {
        public List<Prominence> prominences = new ArrayList<>();
        public List<Sunspot> sunspots = new ArrayList<>();
        public List<Filament> filaments = new ArrayList<>();
    }

    // Placeholders you can flesh out later:
    public static final class Prominence {
        public String id;
        public double angleDeg;   // from solar north, increasing toward east
        public double extentDeg;  // angular width along limb
        public double heightPx;   // radial height beyond limb
        public String label;
    }
    public static final class Sunspot {
        public String id;
        public String group;  // e.g., AR number
        public double angleDeg;
        public double rho;    // 0..1 of disk radius from center
        public double sizePx;
    }
    public static final class Filament {
        public String id;
        // future: path points, etc.
    }
}

