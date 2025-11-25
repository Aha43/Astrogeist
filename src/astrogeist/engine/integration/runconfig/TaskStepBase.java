package astrogeist.engine.integration.runconfig;

import static aha.common.util.Guards.requireNonEmpty;
import static aha.common.util.Guards.requirePositive;

import java.util.UUID;

import aha.common.abstraction.taskrunner.TaskStep;

public abstract class TaskStepBase implements TaskStep {
	
	private final String id = UUID.randomUUID().toString();
	
	private final String label;
	
	private final int weight;
	
	protected TaskStepBase(String label, int weight) {
		requireNonEmpty(label, "label");
		requirePositive(weight, "weight");
		
		this.label = label;
		this.weight = weight;
	}

	@Override public final String id() { return this.id; }
	@Override public final String label() { return this.label; }
	@Override public final int weight() { return this.weight; }
}
