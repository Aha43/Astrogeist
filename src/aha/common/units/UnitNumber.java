package aha.common.units;

import static aha.common.guard.Guards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

import aha.common.util.Strings;

public final class UnitNumber {

    private final double number;
    private final Unit unit;

    public double number() { return number; }
    public Unit unit() { return unit; }

    public int asIntExact() {
        if (number % 1.0 != 0.0) {
            throw new ArithmeticException("Value is not an integer: " + number);
        }
        return (int) number;
    }

    public long asLongExact() {
        if (number % 1.0 != 0.0) {
            throw new ArithmeticException("Value is not an integer: " + number);
        }
        return (long) number;
    }

    public UnitNumber(double number, Unit unit) {
        this.number = number;
        this.unit = requireNonNull(unit, "unit");
    }

    /** Strict: must parse and must have a known unit, or throws. */
    public UnitNumber(String s) {
        requireNonEmpty(s, "s");

        var parsed = Strings.parseNumberWithSuffix(s)
            .orElseThrow(() ->
                new IllegalArgumentException("Cannot parse UnitNumber from: '" +
                	s + "'"));

        this.number = parsed.number();
        this.unit = Unit.fromString(parsed.suffix()); // throws if unknown
    }

    @Override public String toString() { return number + unit.canonical(); }

    /** Lenient: for controllers – no exceptions, just Optional. */
    public static Optional<UnitNumber> tryParse(String s) {
        if (s == null || s.isBlank()) return Optional.empty();

        var parsedOpt = Strings.parseNumberWithSuffix(s);
        if (parsedOpt.isEmpty()) return Optional.empty();

        var parsed = parsedOpt.get();
        var unitOpt = Unit.tryParse(parsed.suffix());
        if (unitOpt.isEmpty()) {
            // you *could* log or collect an error here if you like
            return Optional.empty();
        }

        return Optional.of(new UnitNumber(parsed.number(), unitOpt.get()));
    }
    
}
