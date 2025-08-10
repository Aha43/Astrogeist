package astrogeist.engine.abstraction;

import java.time.Instant;
import java.util.Map;

import astrogeist.engine.timeline.TimelineValue;

public interface WorkflowStep {
	String getName();
	boolean isDone(Map<Instant, Map<String, TimelineValue>> snapshot);
}