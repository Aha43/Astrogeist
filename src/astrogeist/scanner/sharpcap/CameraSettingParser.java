package astrogeist.scanner.sharpcap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CameraSettingParser {
	public static Map<String, String> parseFile(File file) {
	    Map<String, String> map = new LinkedHashMap<>();
	    
	    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	        String line;
	        boolean firstLine = true;

	        while ((line = reader.readLine()) != null) {
	            line = line.trim();
	            if (line.isEmpty()) continue;

	            if (firstLine) {
	                firstLine = false;
	                if (line.startsWith("[") && line.endsWith("]")) {
	                    String cameraType = line.substring(1, line.length() - 1).trim();
	                    map.put("camera", cameraType);
	                }
	                continue;
	            }

	            int equalsIndex = line.indexOf('=');
	            if (equalsIndex != -1) {
	                String key = line.substring(0, equalsIndex).trim();
	                String value = line.substring(equalsIndex + 1).trim();
	                map.put(key, value);
	            }
	        }

	    } catch (IOException e) {
	        System.err.println("Failed to read file: " + file.getAbsolutePath());
	        e.printStackTrace();
	    }

	    return map;
	}
	
	private CameraSettingParser() { throw new AssertionError("Cannot instantiate static class"); }

}
