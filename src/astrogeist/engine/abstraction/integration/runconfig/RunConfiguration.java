package astrogeist.engine.abstraction.integration.runconfig;

public interface RunConfiguration {
	String name();
	int stepCount();
	RunConfigurationStep get(int idx);
}
