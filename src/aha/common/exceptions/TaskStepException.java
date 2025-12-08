package aha.common.exceptions;

import aha.common.abstraction.taskrunner.TaskStep;

/**
 * <p>
 *   Exception thrown by 
 *   {@link TaskStep}.
 * </p>
 */
public final class TaskStepException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param step    the step throwing.
	 * @param message the error message.
	 */
	public TaskStepException(TaskStep step, String message) {
		super(step.label() + " : " + message); }
}
