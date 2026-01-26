package astrogeist.engine.observatory;

import static aha.common.util.Strings.padding;
import static aha.common.util.Strings.quote;

import java.util.List;

import static aha.common.guard.StringGuards.requireNonEmpty;

import aha.common.util.NamedList;

public class Observatory {
	private final String name;
	
	private final NamedList<Instrument> instruments = 
		new NamedList<>(Instrument::name);
	
	private final NamedList<Configuration> configurations =
		new NamedList<>(Configuration::name);
	
	public Observatory() { this("Unnamed"); }
	
	public Observatory(String name) { 
		this.name = requireNonEmpty(name, "name"); }
	
	public final String name() { return this.name; }
	
	public final Observatory addInstrument(Instrument instrument) {
		this.instruments.add(instrument);
		return this;
	}
	
	public final List<Instrument> instruments() {
		return this.instruments.values(); }
	
	public final List<String> instrumentNames() {
		return this.instruments.names(); }
	
	public final Configuration newConfiguration(String name) {
		var retVal = new Configuration(this, name);
		this.configurations.add(retVal);
		return retVal;
	}
	
	public final Configuration newConfiguration(String name, String base) {
		var baseConfiguration = this.configurations.getOrThrow(base);
		var retVal = new Configuration(baseConfiguration, name);
		this.configurations.add(retVal);
		return retVal;
	}
	
	public final List<Configuration> configurations() {
		return this.configurations.values(); }
	
	public final Instrument getInstrument(String name) { 
		return this.instruments.getOrThrow(name); }
	
	@Override public final String toString() {
		var ls = System.lineSeparator();
		var sb = new StringBuilder();
		sb.append("name : ").append(quote(this.name()));
		if (!this.instruments.isEmpty()) {
			sb.append(ls).append("Instruments :");
			for (var ins : this.instruments)
				sb.append(ls).append(padding(4)).append(ins.name());
		}
		if (!this.configurations.isEmpty()) {
			sb.append(ls).append("Configuration :");
			for (var conf : this.configurations)
				sb.append(ls).append(padding(4)).append(conf);
		}
		return sb.toString();
	}
}
