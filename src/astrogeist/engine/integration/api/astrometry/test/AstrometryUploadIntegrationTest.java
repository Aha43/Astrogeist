package astrogeist.engine.integration.api.astrometry.test;

import java.nio.file.Path;
import java.time.Duration;

import astrogeist.engine.integration.api.astrometry.DefaultAstrometryClient;
import astrogeist.engine.integration.api.astrometry.SubmissionPoller;
import astrogeist.engine.integration.api.astrometry.model.UploadOptions;
import astrogeist.engine.integration.api.astrometry.model.Visibility;

public final class AstrometryUploadIntegrationTest {

	private static final String TEST_FOLDER_ENV_VAR = "ASTROMETRY_TEST_FOLDER";
	
	private static String testFolder = "/Users/arnehalvorsen/dev/repos/Astrogeist/test-data";
	
	public static void main(String[] args) {
		
		try {
			var client = new DefaultAstrometryClient();
			var poller = new SubmissionPoller();
		
			// minimal upload with defaults (public, default license flags)
			var subId = client.uploadFile(Path.of(testFolder + "/m13.jpg"), UploadOptions.defaults());
			System.out.println("id : '" + subId + "'");
			var jobIds = poller.pollJobs(subId, Duration.ofSeconds(3), Duration.ofMinutes(5));
			System.out.println("Jobs done");
			for (var jobId : jobIds) {
				System.out.println("jobId : " + jobId);
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
		
	}
}
