package astrogeist.engine.integration.api.astrometry.abstraction;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AstrometrySubmissionPoller {
	List<Integer> pollJobs(long subId, Duration interval, Duration timeout) throws Exception;
	CompletableFuture<List<Integer>> pollJobsAsync(long subId, Duration interval, Duration timeout);
}
