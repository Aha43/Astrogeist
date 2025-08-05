package astrogeist.engine.unittest.fluentassert;

public final class BooleanAssert {
    private final boolean actual;

    public BooleanAssert(boolean actual) {
        this.actual = actual;
    }

    public void isTrue() {
        if (!actual) throw new AssertionError("Expected true but was false");
    }

    public void isFalse() {
        if (actual) throw new AssertionError("Expected false but was true");
    }
}

