package astrogeist.engine.integration.api.astrometry;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Logger;

import astrogeist.engine.integration.api.astrometry.abstraction.AstrometricClient;
import astrogeist.engine.integration.api.astrometry.model.Annotations;
import astrogeist.engine.integration.api.astrometry.model.AstrometricModel;
import astrogeist.engine.integration.api.astrometry.model.Calibration;
import astrogeist.engine.integration.api.astrometry.model.Info;
import astrogeist.engine.integration.api.astrometry.model.MachineTags;
import astrogeist.engine.integration.api.astrometry.model.ObjectsInField;
import astrogeist.engine.integration.api.astrometry.model.Status;
import astrogeist.engine.integration.api.astrometry.model.Tags;
import astrogeist.engine.integration.api.astrometry.model.builder.AnnotationsBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.AstrometricModelBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.CalibrationBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.InfoBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.MachineTagsBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.ObjectsInFieldBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.StatusBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.TagsBuilder;
import astrogeist.engine.logging.Log;

public final class DefaultAstrometricClient implements AstrometricClient {
	private final Logger logger = Log.get(this);
	
	private final AstrometricUris uris = new AstrometricUris();
	private final HttpClient http = HttpClient.newHttpClient();
	
	@Override public final Status getStatus(int jobId) throws Exception {
		return performAstrometricGet(uris.status(jobId), new StatusBuilder()); }
	
	@Override public CompletableFuture<Status> getStatusAsync(int jobId) {
		return performAstrometricGetAsync(uris.status(jobId), new StatusBuilder()); }
	
	@Override public final Info getInfo(int jobId) throws Exception {
		return performAstrometricGet(uris.info(jobId), new InfoBuilder()); }

	@Override public final CompletableFuture<Info> getInfoAsync(int jobId) {
		return performAstrometricGetAsync(uris.info(jobId), new InfoBuilder()); }
	
	@Override public final Calibration getCalibration(int jobId) throws Exception {
		return performAstrometricGet(uris.calibration(jobId), new CalibrationBuilder()); }
	
	@Override public CompletableFuture<Calibration> getCalibrationAsync(int jobId) {
		return performAstrometricGetAsync(uris.calibration(jobId), new CalibrationBuilder()); }
	
	@Override public final Annotations getAnnotations(int jobId) throws Exception {
		return performAstrometricGet(uris.annotations(jobId), new AnnotationsBuilder()); }
	
	@Override public CompletableFuture<Annotations> getAnnotationsAsync(int jobId) {
		return performAstrometricGetAsync(uris.annotations(jobId), new AnnotationsBuilder()); }
	
	@Override public final ObjectsInField getObjectsInField(int jobId) throws Exception {
		return performAstrometricGet(uris.objectsInField(jobId), new ObjectsInFieldBuilder()); }
	
	@Override public CompletableFuture<ObjectsInField> getObjectsInFieldAsync(int jobId) {
		return performAstrometricGetAsync(uris.objectsInField(jobId), new ObjectsInFieldBuilder()); }

	@Override public final MachineTags getMachineTags(int jobId) throws Exception {
		return performAstrometricGet(uris.machineTags(jobId), new MachineTagsBuilder()); }
	
	@Override public CompletableFuture<MachineTags> getMachineTagsAsync(int jobId) {
		return performAstrometricGetAsync(uris.machineTags(jobId), new MachineTagsBuilder()); }

	@Override public final Tags getTags(int jobId) throws Exception {
		return performAstrometricGet(uris.tags(jobId), new TagsBuilder()); }
	
	@Override public CompletableFuture<Tags> getTagsAsync(int jobId) {
		return performAstrometricGetAsync(uris.tags(jobId), new TagsBuilder()); }
	
	private final HttpRequest getAstrometricGetRequest(URI uri) {
		return HttpRequest.newBuilder(uri).GET().build(); }
	
	private final <T extends AstrometricModel> CompletableFuture<T> performAstrometricGetAsync(
		URI uri, AstrometricModelBuilder<T> builder) {
		
		this.logger.info("URI: " + uri);
		
		var req = getAstrometricGetRequest(uri);
		var retVal = this.http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(json -> {
					try {
						return builder.build(json); } catch (Exception e) {
						throw new CompletionException(e);
					}
				});
		
		return retVal;
	}
	
	private final <T extends AstrometricModel>  T performAstrometricGet(
		URI uri, AstrometricModelBuilder<T> builder) throws Exception {
		
		this.logger.info("URI: " + uri);
		
		var req = this.getAstrometricGetRequest(uri);
		var response = this.http.send(req, HttpResponse.BodyHandlers.ofString());
		var json = response.body();
		var retVal = builder.build(json);
		return retVal;
	}

}
