package astrogeist.engine.integration.api.astrometry.test;

import java.nio.file.Path;
import java.util.Map;

import astrogeist.engine.integration.api.astrometry.DefaultAstrometryClient;

public final class AstrometryUploadIntegrationTest {

	private static final String TEST_FOLDER_ENV_VAR = "ASTROMETRY_TEST_FOLDER";
	
	private static String testFolder = "/Users/arnehalvorsen/dev/repos/Astrogeist/test-data";
	
	public static void main(String[] args) {
		
		try {
			var client = new DefaultAstrometryClient();
		
			// minimal upload with defaults (public, default license flags)
			var id = client.uploadFile(Path.of(testFolder + "/m13.jpg"), Map.of());
			System.out.println("id : '" + id + "'");
		} catch (Exception x) {
			x.printStackTrace();
		}
		
	}
}
