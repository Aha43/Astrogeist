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
import astrogeist.engine.integration.api.astrometry.model.Info;
import astrogeist.engine.integration.api.astrometry.model.InfoBuilder;

public final class DefaultAstrometricClient implements AstrometricClient {
	
	private final String baseUri = "https://nova.astrometry.net";
	private final HttpClient http = HttpClient.newHttpClient();
	
	@Override public final Info getInfo(int jobId) throws Exception {
		HttpRequest req = HttpRequest.newBuilder(
				URI.create(this.baseUri + "/api/jobs/" + jobId + "/info/"))
					.GET().build();
		
		var response = this.http.send(req, HttpResponse.BodyHandlers.ofString());
		var json = response.body();
		var retVal = toInfo(json);
		return retVal;
	}

	@Override public final CompletableFuture<Info> getInfoAsync(int jobId) {
		HttpRequest req = HttpRequest.newBuilder(
				URI.create(this.baseUri + "/api/jobs/" + jobId + "/info/"))
					.GET().build();
		
		var retVal = this.http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(json -> {
					try { return toInfo(json); } catch (Exception e) {
						throw new CompletionException(e);
					}
				});
		
		return retVal;
	}
	
	private final Info toInfo(String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(json);
		
		var builder = new InfoBuilder(root.get("status").asText());
		if (builder.isSuccessStatus()) {
			builder
				.withOriginalFileName(root.get("original_filename").asText());
			
			var objectsInField = root.get("objects_in_field");
			for (var node : objectsInField) builder.withObjectInField(node.asText());
			
			var machineTags = root.get("machine_tags");
			for (var node : machineTags) builder.withMachineTag(node.asText());
			
			var tags = root.get("tags");
			for (var node : tags) builder.withTag(node.asText());
			
			var calibration = root.get("calibration");
			builder.calibrationBuilder()
				.withRa(calibration.get("ra").asDouble())
				.withDec(calibration.get("dec").asDouble())
				.withRadius(calibration.get("radius").asDouble())
				.withPixscale(calibration.get("pixscale").asDouble())
				.withOrientation(calibration.get("orientation").asDouble())
				.withParity(calibration.get("parity").asInt());
		}
		
		return builder.build();
	}

}
