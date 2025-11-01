package astrogeist.engine.integration.api.astrometry;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import aha.common.net.HttpUtils;
import astrogeist.engine.integration.api.astrometry.abstraction.AstrometrySubmissionPoller;

public final class DefaultAstrometrySubmissionPoller implements AstrometrySubmissionPoller  {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final HttpClient http;
    private final AstrometryUris uris;

    public DefaultAstrometrySubmissionPoller() { this(null, null); }
    
    public DefaultAstrometrySubmissionPoller(HttpClient http, AstrometryUris uris) {
    	this.http = http == null ? HttpClient.newHttpClient() : http;
        this.uris = uris == null ? new AstrometryUris() : uris;
    }
    
    public final List<Integer> pollJobs(long subId, Duration interval, Duration timeout) throws Exception {
        long deadline = System.nanoTime() + timeout.toNanos();
        while (true) {
            var req = HttpRequest.newBuilder(uris.submissions(subId))
                .header("Accept","application/json")
                .timeout(Duration.ofSeconds(30))
                .GET().build();

            var resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            HttpUtils.ensureSuccess(resp);

            var root = new ObjectMapper().readTree(resp.body());
            
            var status = root.path("status").asText();
            System.out.println("job status : '" + status + "'");

            if ("error".equalsIgnoreCase(status) ||
                "failure".equalsIgnoreCase(status)) {
                throw new RuntimeException("Submission " + subId + ": " +
                    root.path("errormessage").asText("submission failed"));
            }

            List<Integer> jobs = new ArrayList<>();
            var arr = root.path("jobs");
            if (arr.isArray()) for (var n : arr) if (n.isInt()) jobs.add(n.asInt());
            if (!jobs.isEmpty()) return jobs;

            if (System.nanoTime() >= deadline)
                throw new TimeoutException("Timed out waiting for jobs for subid " + subId);

            Thread.sleep(Math.max(250L, interval.toMillis()));
        }
    }

    /**
     * Polls /api/submissions/{subid} until one or more job IDs appear.
     * @param subId       submission id returned by /api/upload
     * @param interval    e.g. Duration.ofSeconds(3)
     * @param timeout     e.g. Duration.ofMinutes(5)
     * @return CompletableFuture of the list of job IDs (non-empty)
     */
    public final CompletableFuture<List<Integer>> pollJobsAsync(
    	long subId, Duration interval, Duration timeout) {

        final long deadline = System.nanoTime() + timeout.toNanos();
        return pollOnce(subId, interval, deadline);
    }

    private final CompletableFuture<List<Integer>> pollOnce(long subId, Duration interval, long deadlineNanos) {
        var req = HttpRequest.newBuilder(uris.submissions(subId))
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(30))
                .GET().build();

        return http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
            .thenApply(resp -> {
                aha.common.net.HttpUtils.ensureSuccess(resp);
                return resp.body();
            })
            .thenCompose(body -> {
                try {
                    JsonNode root = MAPPER.readTree(body);

                    // failure?
                    if ("error".equalsIgnoreCase(root.path("status").asText()) ||
                        "failure".equalsIgnoreCase(root.path("status").asText())) {
                        String msg = root.path("errormessage").asText("submission failed");
                        CompletableFuture<List<Integer>> cf = new CompletableFuture<>();
                        cf.completeExceptionally(new RuntimeException("Submission " + subId + ": " + msg));
                        return cf;
                    }

                    // got jobs?
                    List<Integer> jobs = new ArrayList<>();
                    JsonNode arr = root.path("jobs");
                    if (arr.isArray()) {
                        for (JsonNode n : arr) if (n.isInt()) jobs.add(n.asInt());
                    }
                    if (!jobs.isEmpty()) {
                        return CompletableFuture.completedFuture(jobs);
                    }

                    // not ready yet → delay & retry (if time remains)
                    long now = System.nanoTime();
                    if (now >= deadlineNanos) {
                        CompletableFuture<List<Integer>> cf = new CompletableFuture<>();
                        cf.completeExceptionally(new TimeoutException("Timed out waiting for jobs for subid " + subId));
                        return cf;
                    }

                    long delayMs = Math.max(250L, interval.toMillis());
                    long remainingMs = TimeUnit.NANOSECONDS.toMillis(deadlineNanos - now);
                    if (delayMs > remainingMs) delayMs = remainingMs;
                    
                    return CompletableFuture
                    	    .supplyAsync(() -> null, CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS))
                    	    .thenCompose(x -> pollOnce(subId, interval, deadlineNanos));
                } catch (Exception e) {
                    CompletableFuture<List<Integer>> cf = new CompletableFuture<>();
                    cf.completeExceptionally(e);
                    return cf;
                }
            });
    }
}
