package astrogeist.engine.observatory.types;

import static aha.common.guard.StringGuards.requireNonEmpty;

public record Description(String text) {
	public Description(String text) {
		this.text = requireNonEmpty(text, "text"); }

	@Override public String toString() { return text; }
}
