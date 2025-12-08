package aha.common.units;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import aha.common.util.Strings;

/**
 * <p>
 *   Common units.
 * </p>
 * <p>
 *   Supports canonical string codes (e.g. "mm") and multiple alias spellings.
 * </p>
 * <p>
 *   Usage: {@code Unit u = Unit.fromString("inch");}
 * </p>
 */
public enum Unit {
	NO_UNIT(""),
	
    // --- Length units ---
    MM("mm", "millimeter", "millimetre"),
    CM("cm", "centimeter", "centimetre"),
    M("m", "meter", "metre"),
    INCH("inch", "in"),
    FOOT("ft", "foot", "feet"),

    // --- Angle units ---
    DEG("deg", "degree", "degrees"),
    RAD("rad", "radian", "radians"),
    ARCSEC("arcsec", "arcsecond", "as"),
    ARC_MIN("arcmin", "arcminute", "am"),

    // --- Pixel / resolution ---
    PIXEL("px", "pixel"),
    SUBPIXEL("spx", "subpixel"),

    // --- Time units ---
    MS("ms", "millisecond"),
    S("s", "sec", "second"),
    MIN("min", "minute"),
    HOUR("h", "hr", "hour"),

    // --- Spectral units ---
    NM("nm", "nanometer", "nanometre"),
    ANGSTROM("Å", "angstrom"),

    // --- Weight units ---
    KG("kg", "kilogram"),
    G("g", "gram");

    private final String canonical;
    private final Set<String> aliases;

    Unit(String canonical, String... aliases) {
        this.canonical = canonical;
        Set<String> a = new HashSet<>();
        a.add(canonical.toLowerCase());
        for (var s : aliases) a.add(s.toLowerCase());
        this.aliases = Collections.unmodifiableSet(a);
    }

    public final String canonical() { return canonical; }

    private static final Map<String, Unit> LOOKUP;

    static {
        Map<String, Unit> m = new HashMap<>();
        for (Unit u : values()) {
            for (String alias : u.aliases) {
                m.put(alias.toLowerCase(), u);
            }
        }
        LOOKUP = Map.copyOf(m);
    }

    /**
     * <p>
     *   Parses a string into a Unit.
     * </p>
     * @param s the input string such as "mm", "inch", "deg".
     * @return the Unit enum.
     * @throws IllegalArgumentException if unknown.
     */
    public final static Unit fromString(String s) {
        s = (s == null) ? Strings.EMPTY : s.trim();
        Unit u = LOOKUP.get(s.toLowerCase());
        if (u == null) {
            throw new IllegalArgumentException("Unknown unit: " + s);
        }
        return u;
    }

    /**
     * <p>
     *   Returns all string variants recognized for this unit.
     * </p>
     */
    public final Set<String> aliases() { return aliases; }
    
    /**
     * <p> 
     *   Lenient: returns empty if unknown instead of throwing.
     * </p>
     */
    public static Optional<Unit> tryParse(String s) {
        if (s == null || s.isBlank()) return Optional.empty();
        return Optional.ofNullable(LOOKUP.get(s.toLowerCase()));
    }
    
}
