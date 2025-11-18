package aha.common.unittest.fluentassert;

public final class NumberAssert extends AssertBase{
    private final double actual;

    public NumberAssert(double actual) { this.actual = actual; }

    public final NumberAssert isEqualTo(double expected) {
        if (Double.compare(actual, expected) != 0)
            super.fail("Expected: " + expected + " but got: " + actual);
        return this;
    }

    public final NumberAssert isPositive() {
        if (actual <= 0)
        	super.fail("Expected a positive number but got: " + actual);
        return this;
    }

    public final NumberAssert isNegative() {
        if (actual >= 0)
        	super.fail("Expected a negative number but got: " + actual);
        return this;
    }

    public final NumberAssert isZero() {
        if (actual != 0)
        	super.fail("Expected zero but got: " + actual);
        return this;
    }

    public final NumberAssert isBetween(double min, double max) {
        if (actual < min || actual > max)
        	super.fail("Expected a number between " + min + " and " + max +
        		" but got: " + actual);
        return this;
    }
}
