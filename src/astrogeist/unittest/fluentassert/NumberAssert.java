package astrogeist.unittest.fluentassert;

public final class NumberAssert {
    private final double actual;

    public NumberAssert(double actual) {
        this.actual = actual;
    }

    public NumberAssert isEqualTo(double expected) {
        if (Double.compare(actual, expected) != 0) {
            fail("Expected: " + expected + " but got: " + actual);
        }
        return this;
    }

    public NumberAssert isPositive() {
        if (actual <= 0) {
            fail("Expected a positive number but got: " + actual);
        }
        return this;
    }

    public NumberAssert isNegative() {
        if (actual >= 0) {
            fail("Expected a negative number but got: " + actual);
        }
        return this;
    }

    public NumberAssert isZero() {
        if (actual != 0) {
            fail("Expected zero but got: " + actual);
        }
        return this;
    }

    public NumberAssert isBetween(double min, double max) {
        if (actual < min || actual > max) {
            fail("Expected a number between " + min + " and " + max + " but got: " + actual);
        }
        return this;
    }

    private void fail(String message) {
        throw new AssertionError(message);
    }
}
