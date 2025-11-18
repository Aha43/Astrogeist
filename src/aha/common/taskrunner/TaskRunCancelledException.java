package aha.common.taskrunner;

public final class TaskRunCancelledException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public TaskRunCancelledException() {
        super("Task run cancelled");
    }
}
