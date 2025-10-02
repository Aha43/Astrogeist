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
	Status getStatus(int jobId) throws Exception;
	CompletableFuture<Status> getStatusAsync(int jobId);
	
	Calibration getCalibration(int jobId) throws Exception;
	CompletableFuture<Calibration> getCalibrationAsync(int jobId);
	
	ObjectsInField getObjectsInField(int jobId) throws Exception;
	CompletableFuture<ObjectsInField> getObjectsInFieldAsync(int jobId);
	
	MachineTags getMachineTags(int jobId) throws Exception;
	CompletableFuture<MachineTags> getMachineTagsAsync(int jobId);
	
	Tags getTags(int jobId) throws Exception;
	CompletableFuture<Tags> getTagsAsync(int jobId);
	
	Info getInfo(int jobId) throws Exception;
	CompletableFuture<Info> getInfoAsync(int jobId);
	
	Annotations getAnnotations(int jobId) throws Exception;
	CompletableFuture<Annotations> getAnnotationsAsync(int jobId);
}
