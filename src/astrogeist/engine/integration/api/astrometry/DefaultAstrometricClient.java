package astrogeist.engine.integration.api.astrometry;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Logger;

import astrogeist.common.net.HttpUtils;
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
	
	private final AstrometricUris uris;
	private final HttpClient http;
	
	public DefaultAstrometricClient(
		AstrometricUris uris,
		HttpClient http) {
		
		this.uris = uris == null ? new AstrometricUris() : uris;
		this.http = http == null ? HttpClient.newHttpClient() : http;
	}
	
	@Override public final Status getStatus(long jobId) throws Exception {
		return performAstrometricGet(uris.status(jobId), new StatusBuilder()); }
	
	@Override public CompletableFuture<Status> getStatusAsync(long jobId) {
		return performAstrometricGetAsync(uris.status(jobId), new StatusBuilder()); }
	
	@Override public final Info getInfo(long jobId) throws Exception {
		return performAstrometricGet(uris.info(jobId), new InfoBuilder()); }

	@Override public final CompletableFuture<Info> getInfoAsync(long jobId) {
		return performAstrometricGetAsync(uris.info(jobId), new InfoBuilder()); }
	
	@Override public final Calibration getCalibration(long jobId) throws Exception {
		return performAstrometricGet(uris.calibration(jobId), new CalibrationBuilder()); }
	
	@Override public CompletableFuture<Calibration> getCalibrationAsync(long jobId) {
		return performAstrometricGetAsync(uris.calibration(jobId), new CalibrationBuilder()); }
	
	@Override public final Annotations getAnnotations(long jobId) throws Exception {
		return performAstrometricGet(uris.annotations(jobId), new AnnotationsBuilder()); }
	
	@Override public CompletableFuture<Annotations> getAnnotationsAsync(long jobId) {
		return performAstrometricGetAsync(uris.annotations(jobId), new AnnotationsBuilder()); }
	
	@Override public final ObjectsInField getObjectsInField(long jobId) throws Exception {
		return performAstrometricGet(uris.objectsInField(jobId), new ObjectsInFieldBuilder()); }
	
	@Override public CompletableFuture<ObjectsInField> getObjectsInFieldAsync(long jobId) {
		return performAstrometricGetAsync(uris.objectsInField(jobId), new ObjectsInFieldBuilder()); }

	@Override public final MachineTags getMachineTags(long jobId) throws Exception {
		return performAstrometricGet(uris.machineTags(jobId), new MachineTagsBuilder()); }
	
	@Override public CompletableFuture<MachineTags> getMachineTagsAsync(long jobId) {
		return performAstrometricGetAsync(uris.machineTags(jobId), new MachineTagsBuilder()); }

	@Override public final Tags getTags(long jobId) throws Exception {
		return performAstrometricGet(uris.tags(jobId), new TagsBuilder()); }
	
	@Override public CompletableFuture<Tags> getTagsAsync(long jobId) {
		return performAstrometricGetAsync(uris.tags(jobId), new TagsBuilder()); }
	
	private HttpRequest get(URI uri) {
	    return HttpRequest.newBuilder(uri)
	        .header("Accept", "application/json")
	        .timeout(java.time.Duration.ofSeconds(30))
	        .GET().build();
	 }
	
	private final <T extends AstrometricModel> CompletableFuture<T> performAstrometricGetAsync(
		URI uri, AstrometricModelBuilder<T> builder) {
		
		this.logger.info("URI: " + uri);
		
		var req = get(uri);
		var retVal = this.http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
				.thenApply(response -> {
					HttpUtils.ensureSuccess(response);
					return response.body();
				})
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
		
		var req = this.get(uri);
		var response = this.http.send(req, HttpResponse.BodyHandlers.ofString());
		HttpUtils.ensureSuccess(response);
		var json = response.body();
		var retVal = builder.build(json);
		return retVal;
	}

}
