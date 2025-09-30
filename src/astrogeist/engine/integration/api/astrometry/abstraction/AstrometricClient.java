package astrogeist.engine.integration.api.astrometry.abstraction;

import java.util.concurrent.CompletableFuture;

import astrogeist.engine.integration.api.astrometry.model.Calibration;
import astrogeist.engine.integration.api.astrometry.model.Info;

public interface AstrometricClient {
	public Calibration getCalibration(int jobId) throws Exception;
	public Info getInfo(int jobId) throws Exception;
	public CompletableFuture<Info> getInfoAsync(int jobId);
}
