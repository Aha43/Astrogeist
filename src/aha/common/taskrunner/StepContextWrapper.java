package aha.common.taskrunner;

import aha.common.abstraction.taskrunner.TaskStep;

/**
 * Wraps context to map per-step progress (0-100) into overall percent.
 */
final class StepContextWrapper extends TaskRunContext {

    private final TaskRunContext inner;
    private final TaskStep step;
    private final int baseWeight;
    private final int totalWeight;

    StepContextWrapper(TaskRunContext inner, TaskStep step, int consumed, 
    	int total) {
        
    	super(innerEvent -> {});
    	
        this.inner = inner;
        this.step = step;
        this.baseWeight = consumed;
        this.totalWeight = total;
    }

    @Override public final void log(String message) {
        inner.log("[" + step.id() + "] " + message); }
    @Override public final void error(String message, Throwable t) {
        inner.error("[" + step.id() + "] " + message, t); }

    @Override public final void progress(String stepId, int stepPercent,
    	String message) {
        
    	int stepWeight = step.weight();
        int completedWeight = baseWeight + (stepWeight * Math.max(0,
        	Math.min(stepPercent, 100)) / 100);
        int overall = completedWeight * 100 / totalWeight;
        inner.progress(stepId, overall, message);
    }

    @Override public final void cancel() { inner.cancel(); }
    @Override public final boolean isCancelled() { return inner.isCancelled(); }
    @Override public final void checkCancelled() { inner.checkCancelled(); }
}
