package astrogeist.engine.observatory;

import static aha.common.guard.LogicGuards.throwIf;
import static aha.common.guard.LogicGuards.throwIfNot;
import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.util.Strings.padding;
import static aha.common.util.Strings.quote;
import static java.util.Objects.requireNonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aha.common.util.NamedList;

public final class Axis {
	private final String name;
	
	private final Observatory observatory;
	
	private final NamedList<Configuration> configurations =
			new NamedList<>(Configuration::name);
		
	private Map<String, Configuration> indexedConfigurations = null;
	
	Axis(String name, Observatory observatory) {
		this.name = requireNonEmpty(name, "name");
		this.observatory = requireNonNull(observatory, "observatory");
	}
	
	public final String name() { return this.name; }
	
	public final Observatory observatory() { return this.observatory; }
	
	// - Configurations -
	
	public final Configuration newConfiguration(String name) {
		requireNotClosed();
		var retVal = new Configuration(this.observatory, 
			requireNonEmpty(name, "name"));
		this.configurations.add(retVal);
		return retVal;
	}
	
	public final Configuration newConfiguration(String name,
		Configuration base) {
		
		requireNotClosed();
		
		if (base.observatory() != this.observatory)
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
	
	// - Overrides -
	
	@Override public final String toString() {
		var ls = System.lineSeparator();
		var sb = new StringBuilder();
		if (!this.configurations.isEmpty()) {
			sb.append("name : ").append(quote(this.name()));
			sb.append(ls).append("Configuration :");
			for (var conf : this.configurations)
				sb.append(ls).append(padding(4)).append(conf);
		}
		return sb.toString();
	}

}
