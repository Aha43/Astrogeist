package astrogeist.engine.abstraction.jobs;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import aha.common.async.CancellationSource;

public interface JobRunner extends AutoCloseable {
    <I> JobHandle submit(JobWorker<I> worker, I input,
    	JobProgressListener listener);

    /** Submit multiple at once; returns handles in same order. */
    <I> List<JobHandle> submitAll(List<JobSpec<I>> specs);

    @Override void close(); // shuts down threads

    record JobSpec<I>(JobWorker<I> worker, I input, 
    	JobProgressListener listener) {}

    interface JobHandle {
        String id();
        CancellationSource cancellation();
        // completes when job finishes (success or fail)
        CompletableFuture<Void> completion();
    }
}

