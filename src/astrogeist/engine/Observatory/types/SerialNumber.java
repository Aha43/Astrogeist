package astrogeist.engine.observatory.types;

import static aha.common.guard.StringGuards.requireNonEmpty;

public record SerialNumber(String number) {
	public SerialNumber(String number) {
		this.number = requireNonEmpty(number, "number"); }
	@Override public String toString() { return number; }
}
