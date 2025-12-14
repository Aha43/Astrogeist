package aha.common.numbers;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import aha.common.util.Strings;

import static aha.common.numbers.UnitType.ANGLE;
import static aha.common.numbers.UnitType.LENGTH;
import static aha.common.numbers.UnitType.RESOLUTION;
import static aha.common.numbers.UnitType.TIME;
import static aha.common.numbers.UnitType.VOID;
import static aha.common.numbers.UnitType.WEIGHT;
import static aha.common.numbers.UnitType.SPECTRAL;

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
	NO_UNIT("", VOID),
	
    // --- Length units ---
    MM("mm", LENGTH, "millimeter", "millimetre"),
    CM("cm", LENGTH, "centimeter", "centimetre"),
    M("m", LENGTH, "meter", "metre"),
    INCH("inch", LENGTH, "in"),
    FOOT("ft", LENGTH, "foot", "feet"),

    // --- Angle units ---
    DEG("deg", ANGLE, "degree", "degrees"),
    RAD("rad", ANGLE, "radian", "radians"),
    ARCSEC("arcsec", ANGLE, "arcsecond", "as"),
    ARC_MIN("arcmin", ANGLE, "arcminute", "am"),

    // --- Pixel / resolution ---
    PIXEL("px", RESOLUTION, "pixel"),
    SUBPIXEL("spx", RESOLUTION, "subpixel"),

    // --- Time units ---
    MS("ms", TIME, "millisecond"),
    S("s", TIME, "sec", "second"),
    MIN("min", TIME, "minute"),
    HOUR("h", TIME, "hr", "hour"),

    // --- Spectral units ---
    NM("nm", SPECTRAL, "nanometer", "nanometre"),
    ANGSTROM("Å", SPECTRAL, "angstrom"),

    // --- Weight units ---
    KG("kg", WEIGHT, "kilogram"),
    G("g", WEIGHT, "gram");

	private final UnitType type;
    private final String canonical;
    private final Set<String> aliases;

    private Unit(UnitType type) { this(Strings.EMPTY, type); }
    
    private Unit(String canonical, UnitType type, String... aliases) {
        this.type = type;
    	this.canonical = canonical;
        Set<String> a = new HashSet<>();
        a.add(canonical.toLowerCase());
        for (var s : aliases) a.add(s.toLowerCase());
        this.aliases = Collections.unmodifiableSet(a);
    }
    
    public final UnitType type() { return this.type; }

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
        if (s.length() == 0) return Unit.NO_UNIT;
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
        if (s == null || s.isBlank()) return Optional.of(Unit.NO_UNIT);
        return Optional.ofNullable(LOOKUP.get(s.toLowerCase()));
    }
    
}
