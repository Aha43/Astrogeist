package aha.common.abstraction.taskrunner;

import aha.common.exceptions.TaskStepException;
import aha.common.taskrunner.TaskRunContext;
import aha.common.util.AttributeObject;

public interface TaskStep {    
	/**
	 * <p>
	 *   Stable ID for logging / debugging.
	 * </p>
	 * @return Id.
	 */
	String id();
	
	/**
	 * <p>
	 *   Human readable label for UI.
	 * </p>
	 * @return the label.
	 */
    String label();

    /**
     * <p>
     *   Relative weight for progress (e.g. copying may be 80, invoke siril 20).
     * </p>
     * <p>
     *   All weights are summed by the runner.
     * </p>
     * @return the weight.
     */
    int weight();

    /**
     * <p>
     *   Execute this step. May use context to log, check cancellation, share
     *   data.
     * </p>
     * @throws TaskStepException Throw exception to mark failure.
     */
    void run(TaskRunContext ctx, AttributeObject ctxtData)
    	throws TaskStepException;
}
