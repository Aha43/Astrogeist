package astrogeist.engine.observatory;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import aha.common.util.AttributeBase;
import aha.common.util.Strings;

public final class Instrument extends AttributeBase<Instrument> {
	private final String name;
	
	public Instrument(String name) {
		this.name = requireNonEmpty(name, "name"); }
	
	public Instrument(Instrument o) { 
		super(requireNonNull(o, "o"));
		this.name = o.name();
	}
	
	public String name() { return this.name; }
	
	public Instrument description(String description) {
		return super.set("description", 
			requireNonEmpty(description, "description")); }
	
	public String description() {
		return super.getAsString("description", Strings.EMPTY); }
	
	public Instrument serialNumber(String sn) {
		return super.set("serial-nr", requireNonEmpty(sn, "serial-nr")); }
	
	public String serialNumber() { 
		return super.getAsString("serial-nr", Strings.EMPTY); }
}
