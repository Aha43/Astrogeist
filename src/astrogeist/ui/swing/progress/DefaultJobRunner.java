package astrogeist.ui.swing.progress;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import astrogeist.engine.abstraction.jobs.JobProgressListener;
import astrogeist.engine.abstraction.jobs.JobRunner;
import astrogeist.engine.abstraction.jobs.JobWorker;
import astrogeist.engine.async.CancellationSource;

public final class DefaultJobRunner implements JobRunner {
    private final ExecutorService pool;

    public DefaultJobRunner(int maxParallel) {
        this.pool = Executors.newFixedThreadPool(Math.max(1, maxParallel));}

    @Override public <I> JobHandle submit(JobWorker<I> worker, I input, JobProgressListener listener) {
        Objects.requireNonNull(worker); Objects.requireNonNull(listener);
        var id = UUID.randomUUID().toString();
        var cancelSrc = new CancellationSource();
        var cf = new CompletableFuture<Void>();

        pool.submit(() -> {
            try {
                listener.onStart(-1); // worker may call again with known total
                worker.run(input, listener, cancelSrc.token());
                listener.onDone();
                cf.complete(null);
            } catch (Throwable ex) {
                try { listener.onMessage("Fatal: " + ex.getMessage()); } catch (Throwable ignore) {}
                try { listener.onDone(); } catch (Throwable ignore) {}
                cf.completeExceptionally(ex);
            }
        });

        return new Handle(id, cancelSrc, cf);
    }

    @Override public <I> List<JobHandle> submitAll(List<JobSpec<I>> specs) {
        var handles = new ArrayList<JobHandle>(specs.size());
        for (var s : specs) {
            handles.add(submit(s.worker(), s.input(), s.listener()));
        }
        return handles;
    }

    @Override public void close() { pool.shutdownNow(); }

    // -- impl
    private record Handle(String id, CancellationSource cancellation,
                          CompletableFuture<Void> completion) implements JobHandle {}
}

