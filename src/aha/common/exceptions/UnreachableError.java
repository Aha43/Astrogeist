package aha.common.exceptions;

public final class UnreachableError extends AssertionError {
	private static final long serialVersionUID = 1L;
	public UnreachableError() { super("unreachable"); }
}
