package aha.common.unittest.fluentassert;

import java.util.Objects;

public final class ObjectAssert<T> extends AssertBase {
    private final T actual;

    public ObjectAssert(T actual) { this.actual = actual; }

    public final ObjectAssert<T> isEqualTo(T expected) {
        if (!Objects.equals(actual, expected))
            super.fail("Expected: " + expected + " but got: " + actual);
        return this;
    }

    public ObjectAssert<T> isNotNull() {
        if (actual == null)
        	super.fail("Expected non-null but got null");
        return this;
    }

    public ObjectAssert<T> isNull() {
        if (actual != null)
        	super.fail("Expected null but got: " + actual);
        return this;
    }

}
