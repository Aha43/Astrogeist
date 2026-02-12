package astrogeist.engine.observatory;

import static aha.common.guard.CollectionGuards.requireKeyNotExists;
import static java.util.Objects.requireNonNull;
import static java.util.List.copyOf;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Setup {
	private final Map<String, Configuration> configurations =
		new LinkedHashMap<>(); 
	
	public Setup() {}
	
	public Setup(Setup o) {
		this.configurations.putAll(requireNonNull(o, "o").configurations); }
	
	public final Setup add(Configuration configuration) {
		var axis = requireNonNull(configuration, "configuration").axis();
		var key = requireKeyNotExists(axis.name(), this.configurations);
		this.configurations.put(key, configuration);
		return this;
	}
	
	public final boolean isEmpty() { return this.configurations.isEmpty(); }
	
	public final List<String> getAxisNames() {
		return copyOf(this.configurations.keySet()); }
	
	public final Configuration getConfiguration(String axisName) {
		return this.configurations.get(requireNonNull(axisName, "axisName")); }
	
	public final List<Configuration> getConfigurations() {
		return copyOf(this.configurations.values()); }

}
