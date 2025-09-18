package astrogeist.engine.jobs;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class JobProgress {
    private volatile String name;     // short name (e.g., "Seestar Scanner")
    private volatile String description; // longer description
    private volatile JobStatus status = JobStatus.NOT_STARTED;

    // 0..100
    private final AtomicInteger percent = new AtomicInteger(0);

    // accounting
    private final AtomicInteger okCount = new AtomicInteger(0);
    private final AtomicInteger failCount = new AtomicInteger(0);

    // final message / details (errors, summary, etc.)
    private volatile String details = "";

    public JobProgress(String name) { this.name = Objects.requireNonNull(name); }

    public final String getName() { return name; }
    public final String getDescription() { return description; }
    public final JobStatus getStatus() { return status; }
    public final int getPercent() { return percent.get(); }
    public final int getOkCount() { return okCount.get(); }
    public final int getFailCount() { return failCount.get(); }
    public final String getDetails() { return details; }

    public final JobProgress setName(String name) { this.name = name; return this; }
    public final JobProgress setDescription(String description) {
    	this.description = description; return this; }

    public final void start() { this.status = JobStatus.RUNNING; }
    public final void setPercent(int p) { this.percent.set(Math.max(0, Math.min(100, p))); }
    public final void incOk(int n) { this.okCount.addAndGet(n); }
    public final void incFail(int n) { this.failCount.addAndGet(n); }
    public final void message(String details) { this.details = details == null ? "" : details; }
    
    public synchronized void appendMessage(String line) {
        String prev = this.details == null ? "" : this.details;
        this.details = (prev.isBlank() ? line : prev + System.lineSeparator() + line);
    }

    public final void complete() {
        if (failCount.get() > 0 && okCount.get() > 0) {
            this.status = JobStatus.PARTIAL_SUCCESS;
        } else if (failCount.get() > 0) {
            this.status = JobStatus.FAILED;
        } else {
            this.status = JobStatus.SUCCEEDED;
        }
        this.percent.set(100);
    }
}

