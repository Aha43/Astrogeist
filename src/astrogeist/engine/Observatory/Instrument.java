package astrogeist.engine.Observatory;

import aha.common.units.Unit;
import aha.common.units.UnitNumber;
import aha.common.util.AttributeBase;

public final class Instrument extends AttributeBase<Instrument> {
	public Instrument(String name) { this.set("name", name); }
	public Instrument(Instrument o) { super(o); }
	public String name() { return this.getAsString("name"); }
	
	public final Instrument description(String desc) {
		return super.set("description", desc); }
	public final Instrument serialNumber(String sn) {
		return super.set("serial-number", sn); }
	public final Instrument apperature(int mm) {
		return super.set("apperature", new UnitNumber(mm, Unit.MM)); }
	public final Instrument focalLength(int mm) {
		return super.set("focal-length", new UnitNumber(mm, Unit.MM)); }
}
