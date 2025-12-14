package aha.common.numbers;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.util.Strings.parseNumberWithSuffix;
import static aha.common.util.Strings.quote;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

/**
 * <p>
 *   Models a number with unit.
 * </p>
 * <p>
 *   Unit less numbers are allowed (i.e. {@code unit() -> null} and 
 *   {@code isUnitless() -> true}.
 * </p>
 */
public record UnitNumber(double value, Unit unit) {
	
	
    /**
     * <p>
     *   If is an integer return value as such.
     * </p>
     * @return the value as integer.
     * @throws ArithmeticException If is not an integer.
     */
    public final int asIntExact() {
        if (value % 1.0 != 0.0) {
            throw new ArithmeticException("Value is not an integer: " +
            	quote(value));
        }
        return (int) value;
    }

    /**
     * <p>
     *   If is an integer return value as such.
     * </p>
     * @return the value as integer.
     * @throws ArithmeticException If is not an integer.
     */
    public final long asLongExact() {
        if (value % 1.0 != 0.0) {
            throw new ArithmeticException("Value is not an integer: " +
            	quote(value));
        }
        return (long) value;
    }
    
    /**
     * <p>
     *   Constructor.
     * </p>
     * @param val  Value.
     * @param unit Unit.
     */
    public UnitNumber(double value, Unit unit) {
    	this.unit = requireNonNull(unit, "unit"); this.value = value; }
    
    /**
     * <p>
     *   Creates a
     *   {@link UnitNumber} with unit
     *   {@link Unit#NO_UNIT}.
     * </p>
     * @param val Value.
     */
    public UnitNumber(double val) { this(val, Unit.NO_UNIT); }

    /**
     * <p> 
     *   Strict: must parse and must have a known unit, or throws.
     * </p> 
     */
    public static UnitNumber parse(String s) {
        requireNonEmpty(s, "s");

        var parsed = parseNumberWithSuffix(s)
            .orElseThrow(() ->
                new IllegalArgumentException("Cannot parse UnitNumber from : " +
                	quote(s)));

        var val = parsed.number();
        var unit = Unit.fromString(parsed.suffix());
        return new UnitNumber(val, unit);
    }
	
	/**
     * <p> 
     *   Lenient: for controllers – no exceptions, just Optional.
     * </p>
     */
    public final static Optional<UnitNumber> tryParse(String s) {
        if (s == null || s.isBlank()) return Optional.empty();

        var parsedOpt = parseNumberWithSuffix(s);
        if (parsedOpt.isEmpty()) return Optional.empty();

        var parsed = parsedOpt.get();
        var number = parsed.number();
        var suffix = parsed.suffix();
        var unitOpt = Unit.tryParse(suffix);
        if (unitOpt.isEmpty()) return Optional.empty();
        	
        var unit = unitOpt.get();
        var unitNumber = new UnitNumber(number, unit);
        return Optional.of(unitNumber);
    }
    
}
