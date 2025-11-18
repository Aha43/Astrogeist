package aha.common.taskrunner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Logger;

import aha.common.logging.Log;

public class TaskRunContext {

    private final Logger logger = Log.get(this);
    private final Consumer<TaskEvent> eventSink;
    private final Map<String, Object> data = new ConcurrentHashMap<>();
    private volatile boolean cancelled = false;

    public TaskRunContext(Consumer<TaskEvent> eventSink) { 
    	this.eventSink = eventSink; }

    public Logger logger() { return logger; }

    public void log(String message) {
        logger.info(message);
        eventSink.accept(TaskEvent.log(message));
    }

    public void error(String message, Throwable t) {
        logger.severe(message + " :: " + t.getMessage());
        eventSink.accept(TaskEvent.error(message, t));
    }

    public void put(String key, Object value) { data.put(key, value); }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) { return (T) data.get(key); }

    public void cancel() { this.cancelled = true; }

    public boolean isCancelled() { return cancelled; }

    /** Convenience for steps that want cooperative cancel support. */
    public void checkCancelled() {
        if (cancelled) throw new TaskRunCancelledException(); }

    public void progress(String stepId, int percentWithinStep, String message) {
        eventSink.accept(
        	TaskEvent.progress(stepId, percentWithinStep, message)); }
}

