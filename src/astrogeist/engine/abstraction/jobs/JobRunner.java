package astrogeist.engine.abstraction.jobs;

import java.util.concurrent.CompletableFuture;

import aha.common.async.CancellationSource;

public interface JobRunner extends AutoCloseable {
    <I> JobHandle submit(JobWorker<I> worker, I input, JobProgressListener listener);

    /** Submit multiple at once; returns handles in same order. */
    <I> java.util.List<JobHandle> submitAll(java.util.List<JobSpec<I>> specs);

    @Override void close(); // shuts down threads

    record JobSpec<I>(JobWorker<I> worker, I input, JobProgressListener listener) {}

    interface JobHandle {
        String id();
        CancellationSource cancellation();
        CompletableFuture<Void> completion(); // completes when job finishes (success or fail)
    }
}

