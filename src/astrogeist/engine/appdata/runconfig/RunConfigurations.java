package astrogeist.engine.appdata.runconfig;

import static aha.common.guard.Guards.requireNonEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import astrogeist.engine.abstraction.integration.runconfig.RunConfiguration;

public final class RunConfigurations {
	private final Map<String, RunConfiguration> runConfigurations = 
		new HashMap<>();
	
	final void addConfiguration(RunConfiguration rc) {
		this.runConfigurations.put(rc.name(), rc); }
	
	public final Collection<String> names() { 
		return new HashSet<>(this.runConfigurations.keySet()); }
	
	public final Collection<RunConfiguration> configurations() {
		return new ArrayList<>(this.runConfigurations.values()); }
		
	public final RunConfiguration get(String name) {
		return this.runConfigurations.get(requireNonEmpty(name, "name")); }

}
