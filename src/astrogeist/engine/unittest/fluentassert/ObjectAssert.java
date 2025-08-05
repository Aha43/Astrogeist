package astrogeist.engine.unittest.fluentassert;

import java.util.Objects;

public final class ObjectAssert<T> {
    private final T actual;

    public ObjectAssert(T actual) {
        this.actual = actual;
    }

    public ObjectAssert<T> isEqualTo(T expected) {
        if (!Objects.equals(actual, expected)) {
            fail("Expected: " + expected + " but got: " + actual);
        }
        return this;
    }

    public ObjectAssert<T> isNotNull() {
        if (actual == null) {
            fail("Expected non-null but got null");
        }
        return this;
    }

    public ObjectAssert<T> isNull() {
        if (actual != null) {
            fail("Expected null but got: " + actual);
        }
        return this;
    }

    private void fail(String message) {
        throw new AssertionError(message);
    }
}
