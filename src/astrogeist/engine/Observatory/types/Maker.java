package astrogeist.engine.observatory.types;

import static aha.common.guard.StringGuards.requireNonEmpty;

public record Maker(String name) {
	public Maker(String name) {
		this.name = requireNonEmpty(name, "name"); }
	
	@Override public String toString() { return name; }
}
