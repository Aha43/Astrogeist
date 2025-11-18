package aha.common.taskrunner;

public sealed interface TaskEvent
	permits TaskEvent.Progress, TaskEvent.Log, TaskEvent.Error, TaskEvent.State {

    record Progress(String stepId, int overallPercent, String message) implements TaskEvent {}
    record Log(String message) implements TaskEvent {}
    record Error(String message, Throwable cause) implements TaskEvent {}
    record State(StateType type, String detail) implements TaskEvent {}

    enum StateType { STARTED, STEP_STARTED, STEP_SUCCEEDED, STEP_FAILED, SUCCEEDED, FAILED, CANCELLED }

    static Progress progress(String stepId, int percent, String msg) {
        return new Progress(stepId, percent, msg);
    }

    static Log log(String msg) {
        return new Log(msg);
    }

    static Error error(String msg, Throwable t) {
        return new Error(msg, t);
    }

    static State state(StateType t, String detail) {
        return new State(t, detail);
    }
}
