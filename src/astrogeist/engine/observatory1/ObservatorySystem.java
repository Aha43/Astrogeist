package astrogeist.engine.observatory1;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

public final class ObservatorySystem {
	private final String name;
	private final String displayName;
	
	public ObservatorySystem(String name) {
		this.name = requireNonEmpty(name, "name");
		this.displayName = null;
	}
	
	public ObservatorySystem(String name, String displayName) {
		this.name = requireNonEmpty(name, "name");
		this.displayName = requireNonEmpty(displayName, "displayName");
	}
	
	private final List<Instrument> instruments = new ArrayList<>();
	
	public final ObservatorySystem add(Instrument instrument) {
		this.instruments.add(requireNonNull(instrument, "instrument"));
		return this;
	}
	
	public final String name() { return this.name; }
	
	public final String displayName() { 
		return this.displayName == null ? this.name : this.displayName; }
	
	public final int size() { return this.instruments.size(); }
	public final Instrument get(int idx) { return this.instruments.get(idx); }
	public final int indexOf(Instrument i) { 
		return this.instruments.indexOf(i); }
}
