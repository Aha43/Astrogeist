package aha.common.numbers;

import static aha.common.guard.Guards.requireNonEmpty;
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
public final class UnitNumber {

    private final double number;
    private final Unit unit;

    public final double number() { return this.number; }
    public final Unit unit() { return this.unit; }
    public final boolean isUnitless() { return this.unit == null; }

    /**
     * <p>
     *   If is an integer return value as such.
     * </p>
     * @return the value as integer.
     * @throws ArithmeticException If is not an integer.
     */
    public final int asIntExact() {
        if (number % 1.0 != 0.0) {
            throw new ArithmeticException("Value is not an integer: " +
            	quote(number));
        }
        return (int) number;
    }

    /**
     * <p>
     *   If is an integer return value as such.
     * </p>
     * @return the value as integer.
     * @throws ArithmeticException If is not an integer.
     */
    public final long asLongExact() {
        if (number % 1.0 != 0.0) {
            throw new ArithmeticException("Value is not an integer: " +
            	quote(number));
        }
        return (long) number;
    }
    
    /**
     * <p>
     *   Constructor.
     * </p>
     * @param number Value.
     * @param unit   Unit.
     */
    public UnitNumber(double number, Unit unit) {
    	requireNonNull(unit, "unit");
        this.number = number;
        this.unit = unit;
    }

    /**
     * <p> 
     *   Strict: must parse and must have a known unit, or throws.
     * </p> 
     */
    public UnitNumber(String s) {
        requireNonEmpty(s, "s");

        var parsed = parseNumberWithSuffix(s)
            .orElseThrow(() ->
                new IllegalArgumentException("Cannot parse UnitNumber from : " +
                	quote(s)));

        this.number = parsed.number();
        this.unit = Unit.fromString(parsed.suffix()); // throws if unknown
    }

    @Override public final String toString() {
    	return number + unit.canonical(); }

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
