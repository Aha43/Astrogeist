package astrogeist.engine.abstraction;

import astrogeist.engine.async.CancellationToken;

public interface JobWorker {
    void run(Object input, JobProgressListener listener, CancellationToken token) throws Exception;
}
