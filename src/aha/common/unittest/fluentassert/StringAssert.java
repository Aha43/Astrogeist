package aha.common.unittest.fluentassert;

import static aha.common.util.Strings.quote;

public final class StringAssert extends AssertBase {
    private final String actual;

    public StringAssert(String actual) { this.actual = actual; }

    public StringAssert isEqualTo(String expected) {
        if (!actual.equals(expected))
        	super.fail("Expected " + quote(expected) + " but got: " + 
        		quote(actual));
        return this;
    }

    public StringAssert contains(String substring) {
        if (!actual.contains(substring))
        	super.fail("Expected " + quote(actual) + " to contain " +
        		quote(substring));
        return this;
    }

    public StringAssert startsWith(String prefix) {
        if (!actual.startsWith(prefix))
        	super.fail("Expected " + quote(actual) + " to start with " + 
        		quote(prefix));
        return this;
    }

    public StringAssert endsWith(String suffix) {
        if (!actual.endsWith(suffix))
        	super.fail("Expected " + quote(actual) + " to end with " +
        		quote(suffix));
        return this;
    }
}
