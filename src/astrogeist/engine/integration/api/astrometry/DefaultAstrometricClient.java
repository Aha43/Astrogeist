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
import astrogeist.engine.integration.api.astrometry.model.Calibration;
import astrogeist.engine.integration.api.astrometry.model.CalibrationBuilder;
import astrogeist.engine.integration.api.astrometry.model.Info;
import astrogeist.engine.integration.api.astrometry.model.InfoBuilder;

public final class DefaultAstrometricClient implements AstrometricClient {
	
	private final String baseUri = "https://nova.astrometry.net/api";
	private final HttpClient http = HttpClient.newHttpClient();
	
	@Override public final Info getInfo(int jobId) throws Exception {
		var json = this.performAstrometricGet("/jobs/" + jobId + "/info");
		var builder = new InfoBuilder();
		var retVal = builder.build(json);
		return retVal;
	}

	@Override public final CompletableFuture<Info> getInfoAsync(int jobId) {
		var req = HttpRequest.newBuilder(
				URI.create(this.baseUri + "/api/jobs/" + jobId + "/info/"))
					.GET().build();
		
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
		var json = this.performAstrometricGet("/jobs/" + jobId + "/calibration");
		var builder = new CalibrationBuilder();
		var retVal = builder.build(json);
		return retVal;
	}
	
	private final HttpRequest getAstrometricGetRequest(String uri) {
		var req = HttpRequest.newBuilder(
				URI.create(this.baseUri + uri))
					.GET().build();
		return req;
	}
	
	private final String performAstrometricGet(String uri) throws Exception {
		var req = this.getAstrometricGetRequest(uri);
		var response = this.http.send(req, HttpResponse.BodyHandlers.ofString());
		var json = response.body();
		return json;
	}
	
}
