package astrogeist.engine.unittest.fluentassert;

public final class StringAssert {
    private final String actual;

    public StringAssert(String actual) {
        this.actual = actual;
    }

    public StringAssert isEqualTo(String expected) {
        if (!actual.equals(expected)) {
            fail("Expected: \"" + expected + "\" but got: \"" + actual + "\"");
        }
        return this;
    }

    public StringAssert contains(String substring) {
        if (!actual.contains(substring)) {
            fail("Expected \"" + actual + "\" to contain \"" + substring + "\"");
        }
        return this;
    }

    public StringAssert startsWith(String prefix) {
        if (!actual.startsWith(prefix)) {
            fail("Expected \"" + actual + "\" to start with \"" + prefix + "\"");
        }
        return this;
    }

    public StringAssert endsWith(String suffix) {
        if (!actual.endsWith(suffix)) {
            fail("Expected \"" + actual + "\" to end with \"" + suffix + "\"");
        }
        return this;
    }

    private void fail(String message) {
        throw new AssertionError(message);
    }
}
