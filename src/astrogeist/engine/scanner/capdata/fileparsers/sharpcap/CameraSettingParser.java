package astrogeist.engine.scanner.capdata.fileparsers.sharpcap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import astrogeist.Common;
import astrogeist.engine.logging.Log;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.typesystem.Type;
import astrogeist.engine.typesystem.TypeResolver;

public final class CameraSettingParser {
	private static Logger _logger = Log.get(CameraSettingParser.class);
	
	public static LinkedHashMap<String, TimelineValue> parseFile(File file) {
	    LinkedHashMap<String, TimelineValue> data = new LinkedHashMap<>();
	    
	    try (var reader = new BufferedReader(new FileReader(file))) {
	        var line = "";
	        var firstLine = true;

	        while ((line = reader.readLine()) != null) {
	            line = line.trim();
	            if (line.isEmpty()) continue;

	            if (firstLine) {
	                firstLine = false;
	                if (line.startsWith("[") && line.endsWith("]")) {
	                    var cameraType = line.substring(1, line.length() - 1).trim();
	                    data.put("camera", new TimelineValue(cameraType, Type.Text()));
	                }
	                continue;
	            }

	            int equalsIndex = line.indexOf('=');
	            if (equalsIndex != -1) {
	                var key = line.substring(0, equalsIndex).trim();
	                var value = line.substring(equalsIndex + 1).trim();
	                
	                var type = TypeResolver.resolve(key, value);
	                data.put(key, new TimelineValue(value, type));
	            }
	        }

	    } catch (IOException x) {
	    	_logger.log(Level.SEVERE, "Failed to read file: " + file.getAbsolutePath(), x);
	    }

	    return data;
	}
	
	private CameraSettingParser() { Common.throwStaticClassInstantiateError(); }
}
