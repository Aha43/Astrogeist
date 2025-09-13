package astrogeist.engine.jobs;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class JobProgress {
    private final String id;          // stable key (type+root path, etc.)
    private volatile String name;     // short name (e.g., "Seestar Scanner")
    private volatile String description; // longer description
    private volatile String rootInfo; // e.g., scan root path
    private volatile JobStatus status = JobStatus.NOT_STARTED;

    // 0..100
    private final AtomicInteger percent = new AtomicInteger(0);

    // accounting
    private final AtomicInteger okCount = new AtomicInteger(0);
    private final AtomicInteger failCount = new AtomicInteger(0);

    // final message / details (errors, summary, etc.)
    private volatile String details = "";

    public JobProgress(String id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getRootInfo() { return rootInfo; }
    public JobStatus getStatus() { return status; }
    public int getPercent() { return percent.get(); }
    public int getOkCount() { return okCount.get(); }
    public int getFailCount() { return failCount.get(); }
    public String getDetails() { return details; }

    public JobProgress setName(String name) { this.name = name; return this; }
    public JobProgress setDescription(String description) { this.description = description; return this; }
    public JobProgress setRootInfo(String rootInfo) { this.rootInfo = rootInfo; return this; }

    public void start() { this.status = JobStatus.RUNNING; }
    public void setPercent(int p) { this.percent.set(Math.max(0, Math.min(100, p))); }
    public void incOk(int n) { this.okCount.addAndGet(n); }
    public void incFail(int n) { this.failCount.addAndGet(n); }
    public void message(String details) { this.details = details == null ? "" : details; }

    public void complete() {
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

