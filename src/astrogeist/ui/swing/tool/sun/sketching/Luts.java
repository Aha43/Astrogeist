package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;

import astrogeist.Common;
import astrogeist.ui.swing.tool.abstraction.ColorLut;

public final class Luts {
    private Luts() { Common.throwStaticClassInstantiateError(); }

    // Linear greyscale black → white
    public static final ColorLut GREYSCALE = t -> {
        int v = (int)Math.round(255 * t);
        return new Color(v, v, v);
    };

    // White → deep red
    public static final ColorLut CLASSIC_RED = t -> {
        int r = lerp(255, 170, t);
        int g = lerp(255,   0, t);
        int b = lerp(255,   0, t);
        return new Color(r, g, b);
    };

    // Deep red → orange → yellow (a “sunset” palette)
    public static final ColorLut SUNSET = t -> {
        if (t < 0.5) {
            // 0 → 0.5 : red to orange
            return new Color(
                lerp(170, 255, t * 2),
                lerp(  0, 128, t * 2),
                0
            );
        } else {
            // 0.5 → 1.0 : orange to yellow-white
            double u = (t - 0.5) * 2;
            return new Color(
                255,
                lerp(128, 255, u),
                lerp(0, 180, u)
            );
        }
    };

    // Black → orange → white (sometimes used inverted for prominences)
    public static final ColorLut INVERTED = t -> {
        if (t < 0.5) {
            return new Color(
                lerp(0, 255, t * 2),
                lerp(0, 128, t * 2),
                0
            );
        } else {
            double u = (t - 0.5) * 2;
            return new Color(
                255,
                lerp(128, 255, u),
                lerp(0, 255, u)
            );
        }
    };

    private static int lerp(int a, int b, double t) {
        return (int)Math.round(a + (b - a) * t);
    }
    
    /** Nice ordered list for UI (combo box uses map order). */
    public static Map<String, ColorLut> presets() {
        var m = new LinkedHashMap<String, ColorLut>();
        m.put("Greyscale", GREYSCALE);
        m.put("Classic Red", CLASSIC_RED);
        m.put("Sunset", SUNSET);
        m.put("Inverted", INVERTED);
        return m;
    }
}

