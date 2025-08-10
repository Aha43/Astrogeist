package astrogeist.engine.abstraction;

public interface Workflow {
	String getName();
	void addStep(WorkflowStep step);
	int getStepCount();
	WorkflowStep getStep(int idx);
}
