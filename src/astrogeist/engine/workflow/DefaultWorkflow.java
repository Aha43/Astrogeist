package astrogeist.engine.workflow;

import java.util.ArrayList;
import java.util.List;

import astrogeist.engine.abstraction.Workflow;
import astrogeist.engine.abstraction.WorkflowStep;

public final class DefaultWorkflow implements Workflow {
	private List<WorkflowStep> steps = new ArrayList<>();
	
	private String name;
	
	public DefaultWorkflow(String name) { this.name = name; } 

	@Override public final String getName() { return this.name; }
	@Override public final void addStep(WorkflowStep step) { this.steps.add(step); }
	@Override public final int getStepCount() { return this.steps.size(); }
	@Override public final WorkflowStep getStep(int idx) { return this.steps.get(idx); }
}
