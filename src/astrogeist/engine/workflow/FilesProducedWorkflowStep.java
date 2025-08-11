package astrogeist.engine.workflow;

import java.time.Instant;
import java.util.Map;

import astrogeist.engine.abstraction.WorkflowStep;
import astrogeist.engine.timeline.TimelineValue;

public class FilesProducedWorkflowStep implements WorkflowStep {

	@Override public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override public boolean isDone(Map<Instant, Map<String, TimelineValue>> snapshot) {
		// TODO Auto-generated method stub
		return false;
	}

}
