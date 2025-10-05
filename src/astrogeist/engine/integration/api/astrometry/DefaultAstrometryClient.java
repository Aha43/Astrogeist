package astrogeist.engine.integration.api.astrometry;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import astrogeist.common.Durations;
import astrogeist.common.net.HttpUtils;
import astrogeist.common.net.MultipartBody;
import astrogeist.engine.integration.api.astrometry.abstraction.AstrometryClient;
import astrogeist.engine.integration.api.astrometry.model.Annotations;
import astrogeist.engine.integration.api.astrometry.model.AstrometryModel;
import astrogeist.engine.integration.api.astrometry.model.Calibration;
import astrogeist.engine.integration.api.astrometry.model.Info;
import astrogeist.engine.integration.api.astrometry.model.MachineTags;
import astrogeist.engine.integration.api.astrometry.model.ObjectsInField;
import astrogeist.engine.integration.api.astrometry.model.Status;
import astrogeist.engine.integration.api.astrometry.model.Tags;
import astrogeist.engine.integration.api.astrometry.model.builder.AnnotationsBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.AstrometryModelBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.CalibrationBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.InfoBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.MachineTagsBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.ObjectsInFieldBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.StatusBuilder;
import astrogeist.engine.integration.api.astrometry.model.builder.TagsBuilder;
import astrogeist.engine.logging.Log;

public final class DefaultAstrometryClient implements AstrometryClient {
	private final Logger logger = Log.get(this);
	
	private static final String ENV_VAR = "ASTROMETRY_API_KEY";
	private volatile String session;       // fetched lazily
	private final String apiKey;           // may be null
	
	private final AstrometryUris uris;
	private final AstrometryBodies bodies = new AstrometryBodies();
	private final HttpClient http;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public DefaultAstrometryClient() { this(null, null); }
	
	public DefaultAstrometryClient(
		AstrometryUris uris,
		HttpClient http) {
		
		this.uris = uris == null ? new AstrometryUris() : uris;
		this.http = http == null ? HttpClient.newHttpClient() : http;
		
		this.apiKey = System.getenv(ENV_VAR); // nullable on purpose
	}
	
	// --- GET ---
	
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
	
	private final <T extends AstrometryModel> CompletableFuture<T> performAstrometricGetAsync(
		URI uri, AstrometryModelBuilder<T> builder) {
		
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
	
	private final <T extends AstrometryModel>  T performAstrometricGet(
		URI uri, AstrometryModelBuilder<T> builder) throws Exception {
		
		this.logger.info("URI: " + uri);
		
		var req = this.get(uri);
		var response = this.http.send(req, HttpResponse.BodyHandlers.ofString());
		HttpUtils.ensureSuccess(response);
		var json = response.body();
		var retVal = builder.build(json);
		return retVal;
	}
	
	// -- POST --
	
	public List<Integer> getMyJobs() throws Exception {
		
	    String sess = ensureSession(); // enforces key only here
	    var req = post(this.uris.myJobs(), this.bodies.withSession(sess));
	    
	    var resp = http.send(req, HttpResponse.BodyHandlers.ofString());
	    HttpUtils.ensureSuccess(resp);

	    var root = MAPPER.readTree(resp.body());
	    if ("error".equalsIgnoreCase(root.path("status").asText())) {
	        // session may have expired → refresh once
	        invalidateSession();
	        sess = ensureSession();
	        return getMyJobs(); // simple retry; or factor into a helper if you prefer
	    }

	    var out = new ArrayList<Integer>();
	    var arr = root.path("jobs");
	    if (arr.isArray()) for (var n : arr) out.add(n.asInt());
	    return out;
	}
	
	public int uploadFile(Path file, Map<String,Object> opts) throws Exception {
	    String sess = ensureSession(); // makes sure you have a valid session

	    // Build request-json (session + any extra options)
	    var payload = new java.util.LinkedHashMap<String,Object>();
	    payload.put("session", sess);
	    if (opts != null) payload.putAll(opts);

	    var requestJson = MAPPER.writeValueAsString(payload);

	    // Build multipart body
	    var mp = new MultipartBody();
	    mp.addText("request-json", requestJson)
	      .addFile("file", file);

	    // Build request
	    var req = java.net.http.HttpRequest.newBuilder(uris.upload())
	        .timeout(Durations.OF_60_SECONDS)
	        .header("Content-Type", mp.contentType())
	        .POST(mp.build())
	        .build();

	    // Send sync
	    var resp = http.send(req, HttpResponse.BodyHandlers.ofString());
	    HttpUtils.ensureSuccess(resp);

	    // Parse response for subid
	    return parseSubIdOrThrow(resp.body());
	}

	/** Upload a local file to /api/upload → returns subid */
	@Override public CompletableFuture<Integer> uploadFileAsync(Path file, Map<String, Object> opts) {

	    return ensureSessionAsync().thenCompose(sess -> {
	        // Build request-json (Astrometry expects raw JSON as the value of the form field)
	        var payload = new LinkedHashMap<String,Object>();
	        payload.put("session", sess);
	        if (opts != null) payload.putAll(opts);
	        // Typical options you might pass (strings): allow_commercial_use:"d|y|n", allow_modifications:"d|y|n", publicly_visible:"y|n"
	        // Example: payload.put("allow_commercial_use", "d");

	        String requestJson;
	        try { requestJson = MAPPER.writeValueAsString(payload); }
	        catch (Exception e) { return java.util.concurrent.CompletableFuture.failedFuture(e); }

	        var mp = new MultipartBody();
	        try {
	            mp.addText("request-json", requestJson)
	              .addFile("file", file);
	        } catch (IOException ioe) {
	            return CompletableFuture.failedFuture(ioe);
	        }

	        var req = HttpRequest.newBuilder(uris.upload())
	            .timeout(Durations.OF_60_SECONDS)
	            .header("Content-Type", mp.contentType())
	            .POST(mp.build())
	            .build();

	        return http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
	            .thenApply(HttpUtils::ensureSuccess)
	            .thenApply(HttpResponse::body)
	            .thenApply(this::parseSubIdOrThrow);
	    });
	}

	/** Upload-by-URL via /api/url_upload → returns subid */
	@Override public CompletableFuture<Integer> uploadByUrlAsync(String imageUrl, Map<String, Object> opts) {
	    return ensureSessionAsync().thenCompose(sess -> {
	        var payload = new LinkedHashMap<String,Object>();
	        payload.put("session", sess);
	        payload.put("url", imageUrl);
	        if (opts != null) payload.putAll(opts);

	        String requestJson;
	        try { requestJson = MAPPER.writeValueAsString(payload); }
	        catch (Exception e) { return CompletableFuture.failedFuture(e); }

	        // /api/url_upload expects x-www-form-urlencoded with request-json=<raw-json>
	        var form = "request-json=" + requestJson;

	        var req = java.net.http.HttpRequest.newBuilder(uris.urlUpload())
	            .timeout(Durations.OF_60_SECONDS)
	            .header("Content-Type", "application/x-www-form-urlencoded")
	            .POST(HttpRequest.BodyPublishers.ofString(form, java.nio.charset.StandardCharsets.UTF_8))
	            .build();

	        return http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
	            .thenApply(HttpUtils::ensureSuccess)
	            .thenApply(HttpResponse::body)
	            .thenApply(this::parseSubIdOrThrow);
	    });
	}

	private Integer parseSubIdOrThrow(String body) {
	    try {
	        var root = MAPPER.readTree(body);
	        if (!"success".equalsIgnoreCase(root.path("status").asText()))
	            throw new RuntimeException("Upload failed: " + body);
	        return root.path("subid").asInt();
	    } catch (Exception e) {
	        throw new CompletionException(e);
	    }
	}

	
	private void requireApiKey() {
	    if (apiKey == null || apiKey.isBlank()) {
	        throw new IllegalStateException(
	            ENV_VAR + " not set; this operation requires authentication.");
	    }
	}

	private String ensureSession() throws Exception {
	    requireApiKey();
	    var s = this.session;
	    if (s != null && !s.isBlank()) return s;

	    var req = post(this.uris.login(), this.bodies.login(this.apiKey));

	    var resp = this.http.send(req, HttpResponse.BodyHandlers.ofString());
	    HttpUtils.ensureSuccess(resp);

	    var root = MAPPER.readTree(resp.body());
	    if (!"success".equalsIgnoreCase(root.path("status").asText()))
	        throw new RuntimeException("Login failed: " + resp.body());

	    session = root.path("session").asText();
	    return session;
	}

	private CompletableFuture<String> ensureSessionAsync() {
	    return CompletableFuture.supplyAsync(() -> {
	        try { return ensureSession(); } catch (Exception e) { throw new RuntimeException(e); }
	    });
	}
	
	private HttpRequest post(URI uri, String body) {
		return HttpRequest.newBuilder(uri)
			.header("Content-Type", "application/x-www-form-urlencoded")
			.timeout(Durations.OF_30_SECONDS)
			.POST(HttpRequest.BodyPublishers.ofString(body))
			.build();
	}

	private void invalidateSession() { session = null; }

}
