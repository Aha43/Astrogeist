package astrogeist.engine.appdata.runconfig;

import static aha.common.guard.StringGuards.requireNonEmpty;

import java.util.ArrayList;
import java.util.List;

import astrogeist.engine.abstraction.integration.runconfig.RunConfiguration;
import astrogeist.engine.abstraction.integration.runconfig.RunConfigurationStep;

public final class DefaultRunConfiguration implements RunConfiguration {
	
	private final String name;
	
	private final List<RunConfigurationStep> steps = new ArrayList<>();
	
	public DefaultRunConfiguration(String name) {
		this.name = requireNonEmpty(name, "name"); }
	
	@Override public final String name() { return name; }

	@Override public final int stepCount() { return this.steps.size(); }
		

	@Override public final RunConfigurationStep get(int idx) {
		return this.steps.get(idx); }

}
