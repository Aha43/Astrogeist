package astrogeist.engine.integration.api.astrometry;

import java.net.URI;
import java.util.Objects;

public final class AstrometryUris {
	private static final String DEFAULT_BASE = "https://nova.astrometry.net";
    private static final String ENV_VAR = "ASTROMETRY_BASE_URI";
	
	private final String base;
	
	public AstrometryUris() { 
		this(System.getenv(ENV_VAR) != null ? System.getenv(ENV_VAR) : DEFAULT_BASE); }
	
	public AstrometryUris(String base) {
		Objects.requireNonNull(base, "base");
        if (base.isBlank()) throw new IllegalArgumentException("base");
        // normalize: no trailing slash
        this.base = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
	}
	
	public final String base() { return this.base; }
	
	private final URI complete(String path) { return URI.create(this.base + path); }
	private final String jobStem(long jobId) { return "/api/jobs/" + jobId; }
	
	public final URI login() { return complete("/api/login"); }
	
	public final URI upload() { return complete("/api/upload"); }
	public final URI urlUpload() { return complete("/api/url_upload"); }
	
	public final URI submissions(long subId) { return complete("/api/submissions/" + subId); }
	
	public final URI status(long jobId) { return complete(jobStem(jobId)); }
	public final URI info(long jobId) { return complete(jobStem(jobId) + "/info"); }
	public final URI calibration(long jobId) { return complete(jobStem(jobId) + "/calibration"); }
	public final URI objectsInField(long jobId) { return complete(jobStem(jobId) + "/objects_in_field"); }
	public final URI tags(long jobId) { return complete(jobStem(jobId) + "/tags"); }
	public final URI machineTags(long jobId) { return complete(jobStem(jobId) + "/machine_tags"); }
	public final URI annotations(long jobId) { return complete(jobStem(jobId) + "/annotations"); }
	
	public final URI joblog(long jobId) { return complete("/api/joblog/" + jobId); }
	public final URI joblog2(long jobId) { return complete("/api/joblog2/" + jobId); }
	
	public final URI myJobs() { return complete("/api/myjobs"); }
	public final URI submissionImages() { return complete("/api/submission_images"); }
	public final URI jobsByTag() { return complete("/api/jobs_by_tag"); }
	
	@Override public final String toString() { return base(); }
}
