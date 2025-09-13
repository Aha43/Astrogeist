package astrogeist.engine.jobs;

public enum JobStatus {
    NOT_STARTED,
    RUNNING,
    SUCCEEDED,
    FAILED,
    PARTIAL_SUCCESS; // useful when okCount>0 && failCount>0
}
