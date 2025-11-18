package aha.common.unittest.fluentassert;

public final class BooleanAssert extends AssertBase {
    private final boolean actual;

    public BooleanAssert(boolean actual) { this.actual = actual; }

    public final void isTrue() {
        if (!actual) super.fail("Expected true but was false"); }
    public final void isFalse() {
        if (actual) super.fail("Expected false but was true"); }
}

