package lab;


import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lab.AssembledTimestampResolver.TimestampPattern;

public class TestTimestampResolver {
    public static void main(String[] args) {
        try {
            // Load XML file from disk (adjust path as needed)
            File configFile = new File("/Users/arnehalvorsen/dev/repos/Astrogeist/scan-timestamps.xml");

            // Parse all timestamp patterns
            List<TimestampPattern> patterns = AssembledTimestampResolver.parseXml(configFile);

            // Example path to test
            var paths = new ArrayList<Path>();
            //paths.add(Path.of("/Volumes/Extreme SSD/CapObj/2025-07-27_09_45_01Z/2025-07-27-0945_0-CapObj_0165.FIT.txt"));
            paths.add(Path.of("/Volumes/Extreme SSD/SharpCap/2025-05-04/Sun/16_06_08.CameraSettings.txt"));
            //paths.add(Path.of("/Volumes/Extreme SSD/SharpCap/2025-05-04/Sun/"));
            

            // Try all patterns until one works
            for (var path : paths) {
            	for (TimestampPattern pattern : patterns) {
            		Optional<Instant> maybeInstant = pattern.extract(path);
            		if (maybeInstant.isPresent()) {
            			System.out.println("✅ Timestamp extracted: " + maybeInstant.get());
            		}
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

