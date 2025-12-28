package astrogeist.engine.observatory;

import static aha.common.guard.CollectionGuards.requireNoDuplicate;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

public final class Configurations {
	private final List<Configuration> configurations = new ArrayList<>();
	
	public final Configurations add(Configuration c) {
		this.configurations.add(
			requireNoDuplicate(c, this.configurations, "c"));
		return this;
	}
	
	public final int size() { return this.configurations.size(); }
	
	public final Configuration get(int idx) { 
		return this.configurations.get(idx); }
	
	public final int indexOf(Configuration configuration) {
		return this.configurations.indexOf(
			requireNonNull(configuration, "configuration")); }
}
