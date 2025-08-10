package astrogeist.engine.abstraction;

public interface Workflow {
	String getName();
	int getStepCount();
	WorkflowStep getStep(int idx);
}
