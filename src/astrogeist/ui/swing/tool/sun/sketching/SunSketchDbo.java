// FILE: astrogeist/sun/sketching/dbo/SunSketchDbo.java
// (only the Sunspot type changed; rest unchanged)

package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class SunSketchDbo {
    public String schema = "astrogeist.sun.sketch/v1";
    public String id = UUID.randomUUID().toString();
    public Instant createdUtc;
    public Instant modifiedUtc;

    public Canvas canvas = new Canvas();
    public SunStyle sunStyle = new SunStyle();
    public Features features = new Features();

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
        public double t;
        public LutRef() {}
        public LutRef(String name, double t) { this.name = name; this.t = t; }
    }

    public static final class Features {
        public List<Prominence> prominences = new ArrayList<>();
        public List<Sunspot> sunspots = new ArrayList<>();
        public List<Filament> filaments = new ArrayList<>();
    }

    public static final class Prominence {
        public String id;
        public double angleDeg;   // 0°=North, 90°=East
        public double extentDeg;  // angular width along limb
        public double heightR;    // height beyond limb in R☉
        public String label;
    }

    public static final class Sunspot {
        public String id;         // optional; we’ll fill at save time
        public String group;      // optional AR tag
        public double angleDeg;   // 0°=North, 90°=East
        public double rho;        // 0..1 on disk
        public double sizeR;      // radius in R☉  (normalized)
    }

    public static final class Filament {
        public String id;
        // future: path points, etc.
    }
}
