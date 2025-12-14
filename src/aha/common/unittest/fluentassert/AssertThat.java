package aha.common.unittest.fluentassert;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;;

public final class AssertThat {
	private AssertThat() { throwStaticClassInstantiateError(); }
	
    public final static NumberAssert that(double actual) {
        return new NumberAssert(actual); }
    public final static StringAssert that(String actual) {
        return new StringAssert(actual); }
    public final static BooleanAssert that(boolean actual) {
        return new BooleanAssert(actual); }
    public final static <T> ObjectAssert<T> that(T actual) {
        return new ObjectAssert<>(actual); }
}

