package astrogeist.engine.appdata.runconfig;

import aha.common.guard.Guards;
import astrogeist.engine.abstraction.integration.runconfig.RunConfigurationStep;

public abstract class AbstractRunConfigurationStep
	implements RunConfigurationStep {

	private final String name;
	
	protected AbstractRunConfigurationStep(String name) {
		this.name = Guards.requireNonEmpty(name, "name"); }
	
	@Override public final String name() { return this.name; }
	
}
