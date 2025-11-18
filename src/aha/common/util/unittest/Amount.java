package aha.common.util.unittest;

public class Amount {
    private final int value;
	public Amount(String s) { this.value = Integer.parseInt(s); }
	public int value() { return this.value; }
}
