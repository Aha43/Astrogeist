package aha.common.util.unittest;

/**
 * <p>
 *   Type used in unit test. Not for any other use.
 * </p>
 */
public final class Amount {
    private final int value;
	public Amount(String s) { this.value = Integer.parseInt(s); }
	public final int value() { return this.value; }
}
