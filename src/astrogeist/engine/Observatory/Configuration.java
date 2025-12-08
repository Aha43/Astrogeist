package astrogeist.engine.Observatory;

import static java.util.Objects.requireNonNull;
import static aha.common.util.Strings.padding;
import static aha.common.guard.CollectionGuards.requireKeyNotExists;
import static java.lang.String.join;
import static java.lang.System.lineSeparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Configuration {
	private final InstrumentNode root;
	
	private final Map<String, List<Instrument>> cofigurations = new HashMap<>();
	
	public Configuration(InstrumentNode root) {
		requireNonNull(root, "root");
		this.root = root;
	}
	
	public Configuration addSystem(String system) {
		requireKeyNotExists(system, this.cofigurations, "system");
		var instruments = root.system(system);
		this.cofigurations.put(system, instruments);
		return this;
	}
	
	public final String name() { 
		return join(".", this.cofigurations.keySet()); }
	
	@Override public final String toString() {
		var ls = lineSeparator();
		var stbu = new StringBuilder(this.name()).append(ls);
		for (var e : this.cofigurations.entrySet())
			stbu.append(padding(2))
				.append(e.getKey())
				.append(ls)
				.append(padding(4))
				.append(e.getValue())
				.append(ls);
		return stbu.toString();
	}
	
}
