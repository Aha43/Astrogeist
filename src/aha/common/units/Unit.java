package aha.common.units;

import java.util.*;

/**
 * <p>
 *   Common units.
 * </p>
 * <p>
 *   Supports canonical string codes (e.g. "mm") and multiple alias spellings.
 * </p>
 * <p>
 * Usage:
 *     Unit u = Unit.fromString("inch");
 * </p>
 */
public enum Unit {

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

    // --- Misc for inventory ---
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

    public String canonical() { return canonical; }

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
     * Parses a string into a Unit.
     *
     * @param s the input string such as "mm", "inch", "deg"
     * @return the Unit enum
     * @throws IllegalArgumentException if unknown
     */
    public static Unit fromString(String s) {
        Objects.requireNonNull(s, "Unit string cannot be null");
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
    public Set<String> aliases() { return aliases; }
    
    /** Lenient: returns empty if unknown instead of throwing. */
    public static Optional<Unit> tryParse(String s) {
        if (s == null || s.isBlank()) return Optional.empty();
        return Optional.ofNullable(LOOKUP.get(s.toLowerCase()));
    }
    
}
