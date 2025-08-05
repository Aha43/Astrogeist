package astrogeist.engine.unittest.fluentassert;

public final class AssertThat {
    public static NumberAssert that(double actual) {
        return new NumberAssert(actual);
    }

    public static StringAssert that(String actual) {
        return new StringAssert(actual);
    }

    public static BooleanAssert that(boolean actual) {
        return new BooleanAssert(actual);
    }

    public static <T> ObjectAssert<T> that(T actual) {
        return new ObjectAssert<>(actual);
    }

    private AssertThat() {}
}

