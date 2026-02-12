package astrogeist.engine.observatory;

import static aha.common.guard.CollectionGuards.requireKeyNotExists;
import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.util.Default.orDefault;
import static java.util.Objects.requireNonNull;

import java.util.List;

import aha.common.util.NamedList;

public class Observatory {
	
	private final NamedList<Axis> axises = new NamedList<>(Axis::name);
	
	private final String name;
	
	private final String defaultAxisName;
	
	public Observatory() { this("Unnamed", null); }
	
	public Observatory(String name) { this(name, null); }
	
	public Observatory(String name, String defaultAxisName) { 
		this.name = requireNonEmpty(name, "name");
		this.defaultAxisName = orDefault(defaultAxisName, "Optical");
		var defaultAxis = new Axis(this.defaultAxisName, this);
		this.axises.add(defaultAxis);
	}
	
	public final String name() { return this.name; }
	
	// - Instruments -
	
	public final Observatory addInstrument(Instrument instrument) {
		this.defaultAxis().addInstrument(
			requireNonNull(instrument, "instrument"));
		return this;
	}
	
	public final List<Instrument> instruments() {
		return this.defaultAxis().instruments(); }
	
	public final List<String> instrumentNames() {
		return this.defaultAxis().instrumentNames(); }
	
	public final Instrument getInstrument(String name) { 
		return this.defaultAxis().getInstrument(name); }
	
	public final boolean hasInstrument(Instrument instrument) {
		return this.defaultAxis().hasInstrument(instrument); }
	
	// - Configurations (default axis) -

	public final Axis defaultAxis() {
		return this.axises.get(this.defaultAxisName); }
	
	public final Configuration newConfiguration(String name) {
		return this.defaultAxis().newConfiguration(name); }
	
	public final Configuration newConfiguration(String name,
			Configuration base) {
		return this.defaultAxis().newConfiguration(name, base); }
	
	public final Configuration newConfiguration(String name, String base) {
		return this.defaultAxis().newConfiguration(name, base); }
	
	public final Configuration getConfiguration(String name) {
		return this.defaultAxis().getConfiguration(name); }
	
	public final List<Configuration> configurations() {
		return this.defaultAxis().configurations(); }
	
	public final void close() { this.defaultAxis().close(); }
	
	public final Configuration getConfigurationById(String id) {
		return this.defaultAxis().getConfigurationById(id); }
	
	// - Axis -
	
	public final Axis getAxis(String name) {
		return this.axises.get(requireNonEmpty(name, "name")); }
	
	public final List<String> getAxisNames() { return this.axises.names(); }
	
	public final List<Axis> getAxises() { 
		var retVal = this.axises.values();
		return retVal;
	}
	
	public final int getAxisCount() { return this.axises.size(); }
	
	public final int getIndexOfAxis(Axis axis) { 
		return this.axises.indexOf(axis); }
	
	public final Axis newAxis(String name) {
		requireKeyNotExists(name, this.axises, "name");
		var retVal = new Axis(name, this);
		this.axises.add(retVal);
		return retVal;
	}
	
	// - Overrides -
	
	@Override public final String toString() {
//		var ls = System.lineSeparator();
//		var sb = new StringBuilder();
//		sb.append("name : ").append(quote(this.name()));
//		if (!this.instruments.isEmpty()) {
//			sb.append(ls).append("Instruments :");
//			for (var ins : this.instruments)
//				sb.append(ls).append(padding(4)).append(ins.name());
//		}
//		sb.append(this.defaultAxis());
//		return sb.toString();
		return "";
	}
	
}
