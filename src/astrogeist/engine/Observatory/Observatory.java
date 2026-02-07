package astrogeist.engine.observatory;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.util.Strings.padding;
import static aha.common.util.Strings.quote;
import static java.util.Objects.requireNonNull;
import static aha.common.guard.LogicGuards.throwIf;
import static aha.common.guard.LogicGuards.throwIfNot;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aha.common.util.NamedList;

public class Observatory {
	private final String name;
	
	private final NamedList<Instrument> instruments = 
		new NamedList<>(Instrument::name);
	
	private final NamedList<Configuration> configurations =
		new NamedList<>(Configuration::name);
	
	private Map<String, Configuration> indexedConfigurations = null;
	
	public Observatory() { this("Unnamed"); }
	
	public Observatory(String name) { 
		this.name = requireNonEmpty(name, "name"); }
	
	public final String name() { return this.name; }
	
	public final Observatory addInstrument(Instrument instrument) {
		requireNotClosed();
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
	
	public final Configuration newConfiguration(String name) {
		requireNotClosed();
		var retVal = new Configuration(this, requireNonEmpty(name, "name"));
		this.configurations.add(retVal);
		return retVal;
	}
	
	public final Configuration newConfiguration(String name,
		Configuration base) {
		
		requireNotClosed();
		
		if (base.observatory() != this)
			throw new IllegalArgumentException(
				"Base configuration must belong to this observatory");
		
		var retVal = new Configuration(requireNonNull(base, "base"),
			requireNonEmpty(name, "name"));
		this.configurations.add(retVal);
		return retVal;
	}
	
	public final Configuration newConfiguration(String name, String base) {
		requireNotClosed();
		var baseConfiguration = this.configurations.getOrThrow(
			requireNonNull(base, "base"));
		return newConfiguration(name, baseConfiguration);
	}
	
	public final Configuration getConfiguration(String name) {
		return this.configurations.get(requireNonEmpty(name, "name")); }
	
	public final List<Configuration> configurations() {
		return this.configurations.values(); }
	
	public final Instrument getInstrument(String name) { 
		return this.instruments.getOrThrow(requireNonEmpty(name, "name")); }
	
	public final boolean hasInstrument(Instrument instrument) {
		var found = this.getInstrument(
			requireNonNull(instrument, "instrument").name());
		return found == instrument;
	}
	
	public final void close() {
		requireNotClosed();
		this.indexedConfigurations =
			new LinkedHashMap<>(this.configurations.size());
		for (var curr : this.configurations)
			this.indexedConfigurations.put(curr.id(), curr);
	}
	
	public final Configuration getConfigurationById(String id) {
		requireNonEmpty(id, "id");
		requireClosed();
		return this.indexedConfigurations.get(id);
	}
	
	private final void requireClosed() { 
		throwIf(this.indexedConfigurations == null, "Not closed");
	}
	
	private final void requireNotClosed() { 
		throwIfNot(this.indexedConfigurations == null, "Closed");
	}
	
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
