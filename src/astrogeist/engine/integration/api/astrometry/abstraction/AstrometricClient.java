package astrogeist.engine.integration.api.astrometry.abstraction;

import java.util.concurrent.CompletableFuture;

import astrogeist.engine.integration.api.astrometry.model.Annotations;
import astrogeist.engine.integration.api.astrometry.model.Calibration;
import astrogeist.engine.integration.api.astrometry.model.Info;
import astrogeist.engine.integration.api.astrometry.model.MachineTags;
import astrogeist.engine.integration.api.astrometry.model.ObjectsInField;
import astrogeist.engine.integration.api.astrometry.model.Status;
import astrogeist.engine.integration.api.astrometry.model.Tags;

public interface AstrometricClient {
	Status getStatus(long jobId) throws Exception;
	CompletableFuture<Status> getStatusAsync(long jobId);
	
	Calibration getCalibration(long jobId) throws Exception;
	CompletableFuture<Calibration> getCalibrationAsync(long jobId);
	
	ObjectsInField getObjectsInField(long jobId) throws Exception;
	CompletableFuture<ObjectsInField> getObjectsInFieldAsync(long jobId);
	
	MachineTags getMachineTags(long jobId) throws Exception;
	CompletableFuture<MachineTags> getMachineTagsAsync(long jobId);
	
	Tags getTags(long jobId) throws Exception;
	CompletableFuture<Tags> getTagsAsync(long jobId);
	
	Info getInfo(long jobId) throws Exception;
	CompletableFuture<Info> getInfoAsync(long jobId);
	
	Annotations getAnnotations(long jobId) throws Exception;
	CompletableFuture<Annotations> getAnnotationsAsync(long jobId);
}
