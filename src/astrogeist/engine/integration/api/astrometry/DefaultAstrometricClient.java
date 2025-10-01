package astrogeist.engine.integration.api.astrometry;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import astrogeist.engine.integration.api.astrometry.abstraction.AstrometricClient;
import astrogeist.engine.integration.api.astrometry.model.Annotations;
import astrogeist.engine.integration.api.astrometry.model.AnnotationsBuilder;
import astrogeist.engine.integration.api.astrometry.model.AstrometricModel;
import astrogeist.engine.integration.api.astrometry.model.AstrometricModelBuilder;
import astrogeist.engine.integration.api.astrometry.model.Calibration;
import astrogeist.engine.integration.api.astrometry.model.CalibrationBuilder;
import astrogeist.engine.integration.api.astrometry.model.Info;
import astrogeist.engine.integration.api.astrometry.model.InfoBuilder;

public final class DefaultAstrometricClient implements AstrometricClient {
	
	private final AstrometricUris uris = new AstrometricUris();
	private final HttpClient http = HttpClient.newHttpClient();
	
	@Override public final Info getInfo(int jobId) throws Exception {
		var builder = new InfoBuilder();
		var retVal = performAstrometricGet(uris.info(jobId), builder);
		return retVal;
	}

	@Override public final CompletableFuture<Info> getInfoAsync(int jobId) {
		var req = getAstrometricGetRequest(uris.info(jobId));
		var retVal = this.http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(json -> {
					try {
						var builder = new InfoBuilder();
						return builder.build(json); } catch (Exception e) {
						throw new CompletionException(e);
					}
				});
		
		return retVal;
	}

	@Override public Calibration getCalibration(int jobId) throws Exception {
		var builder = new CalibrationBuilder();
		var retVal = performAstrometricGet(uris.calibration(jobId), builder);
		return retVal;
	}
	
	@Override public Annotations getAnnotations(int jobId) throws Exception {
		var builder = new AnnotationsBuilder();
		var retVal = performAstrometricGet(uris.annotations(jobId), builder);
		return retVal;
	}
	
	private final HttpRequest getAstrometricGetRequest(URI uri) {
		var req = HttpRequest.newBuilder(uri).GET().build();
		return req;
	}
	
	private final <T extends AstrometricModel>  T performAstrometricGet(URI uri, AstrometricModelBuilder<T> builder) throws Exception {
		var req = this.getAstrometricGetRequest(uri);
		var response = this.http.send(req, HttpResponse.BodyHandlers.ofString());
		var json = response.body();
		var retVal = builder.build(json);
		return retVal;
	}

	
	
}
