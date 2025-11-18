package aha.common.unittest.fluentassert;

public abstract class AssertBase {
	protected final void fail(String message) {
		throw new AssertionError(message); }
}
