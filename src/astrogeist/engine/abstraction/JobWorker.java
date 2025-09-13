package astrogeist.engine.abstraction;

import astrogeist.engine.async.CancellationToken;

public interface JobWorker<I> {
	String name();
	String description();
    void run(I input, JobProgressListener listener, CancellationToken token) throws Exception;
}
