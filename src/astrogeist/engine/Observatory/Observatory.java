package astrogeist.engine.observatory;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.util.Strings.padding;
import static aha.common.util.Strings.quote;
import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aha.common.util.NamedList;

public class Observatory {
	private final Axis defaultAxis = new Axis("default", this);
	
	private final String name;
	
	private final NamedList<Instrument> instruments = 
		new NamedList<>(Instrument::name);
	
	public Observatory() { this("Unnamed"); }
	
	public Observatory(String name) { 
		this.name = requireNonEmpty(name, "name"); }
	
	public final String name() { return this.name; }
	
	public final Observatory addInstrument(Instrument instrument) {
		this.instruments.add(requireNonNull(instrument, "instrument"));
		return this;
	}
	
	public final List<Instrument> instruments() {
		return this.instruments.values(); }
	
	public final List<String> instrumentNames() {
		return this.instruments.names(); }
	
	public final Set<String> allTags() {
		var tmp = new HashSet<String>();
		for (var curr : this.instruments) tmp.addAll(curr.tags());
		return Set.copyOf(tmp);
	}
	
	// - Instruments -
	
	public final Instrument getInstrument(String name) { 
		return this.instruments.getOrThrow(requireNonEmpty(name, "name")); }
	
	public final boolean hasInstrument(Instrument instrument) {
		var found = this.getInstrument(
			requireNonNull(instrument, "instrument").name());
		return found == instrument;
	}
	
	// - Configurations -
	
	public final Configuration newConfiguration(String name) {
		return this.defaultAxis.newConfiguration(name); }
	
	public final Configuration newConfiguration(String name,
		Configuration base) {
		
		return this.defaultAxis.newConfiguration(name, base);
	}
	
	public final Configuration newConfiguration(String name, String base) {
		return this.defaultAxis.newConfiguration(name, base); }
	
	public final Configuration getConfiguration(String name) {
		return this.defaultAxis.getConfiguration(name); }
	
	public final List<Configuration> configurations() {
		return this.defaultAxis.configurations(); }
	
	public final void close() { this.defaultAxis.close(); }
	
	public final Configuration getConfigurationById(String id) {
		return this.defaultAxis.getConfigurationById(id); }
	
	// - Overrides -
	
	@Override public final String toString() {
		var ls = System.lineSeparator();
		var sb = new StringBuilder();
		sb.append("name : ").append(quote(this.name()));
		if (!this.instruments.isEmpty()) {
			sb.append(ls).append("Instruments :");
			for (var ins : this.instruments)
				sb.append(ls).append(padding(4)).append(ins.name());
		}
		sb.append(this.defaultAxis);
		return sb.toString();
	}
	
}
