package aha.common.taskrunner;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import aha.common.abstraction.taskrunner.TaskStep;
import aha.common.logging.Log;
import aha.common.util.AttributeObject;

public final class TaskRunner {
	private final Logger logger = Log.get(this);

    private final List<TaskStep> steps;
    private final Consumer<TaskEvent> eventSink;
    
    private final TaskRunContext ctx;

    public TaskRunner(List<? extends TaskStep> steps,
    	Consumer<TaskEvent> eventSink) {
        
    	if (steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException(
            	"At least one TaskStep is required");
        }
        this.steps = List.copyOf(steps);
        this.eventSink = eventSink;
        
        this.ctx = new TaskRunContext(eventSink);
    }
    
    public final TaskRunContext context() { return this.ctx; }

    public void run(AttributeObject contextData) {
        eventSink.accept(TaskEvent.state(TaskEvent.StateType.STARTED,
        	"Task run started"));

        int totalWeight = steps.stream().mapToInt(TaskStep::weight).sum();
        int consumedWeight = 0;

        try {
            for (TaskStep step : steps) {
                ctx.checkCancelled();

                eventSink.accept(TaskEvent.state(
                	TaskEvent.StateType.STEP_STARTED,
                	step.label()
                ));

                int stepWeight = step.weight();
                
                var scw = new StepContextWrapper(this.ctx, step, consumedWeight,
                    totalWeight);
                
                step.run(scw, contextData);

                consumedWeight += stepWeight;

                eventSink.accept(TaskEvent.state(
                        TaskEvent.StateType.STEP_SUCCEEDED,
                        step.label()
                ));

                int overall = consumedWeight * 100 / totalWeight;
                eventSink.accept(TaskEvent.progress(step.id(), overall,
                	step.label() + " done"));
            }

            if (ctx.isCancelled()) {
                eventSink.accept(TaskEvent.state(TaskEvent.StateType.CANCELLED,
                	"Cancelled"));
                this.logger.info("cancelled");
            } else {
                eventSink.accept(TaskEvent.state(TaskEvent.StateType.SUCCEEDED,
                	"All steps completed"));
                this.logger.info("all steps completed");
            }

        } catch (TaskRunCancelledException cancel) {
        	this.logger.info("Cancelled : caught '" +
        		TaskRunCancelledException.class.getSimpleName() + 
        			"' exception");
            eventSink.accept(TaskEvent.state(TaskEvent.StateType.CANCELLED,
            	"Cancelled"));
        } catch (Exception ex) {
        	Log.error(this.logger, ex);
            eventSink.accept(TaskEvent.error("Task run failed", ex));
            eventSink.accept(TaskEvent.state(TaskEvent.StateType.FAILED,
            	ex.getMessage()));
        }
    }

}
