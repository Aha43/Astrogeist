package astrogeist.timeline;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import astrogeist.Common;

public final class TimelineUtil {

	public static List<TimelineValue> getOfType(LinkedHashMap<String, TimelineValue> data, String type){
		return data.values().stream().filter(v -> type.equals(v.type())).collect(Collectors.toList()); }
	
	public static List<TimelineValue> getExcludingType(LinkedHashMap<String, TimelineValue> data, String type){
		return data.values().stream().filter(v -> !type.equals(v.type())).collect(Collectors.toList()); }
	
	public static LinkedHashMap<String, TimelineValue> getExcludingTypeMap(
	        LinkedHashMap<String, TimelineValue> data, String type) {
	    
	    LinkedHashMap<String, TimelineValue> result = new LinkedHashMap<>();
	    for (Entry<String, TimelineValue> entry : data.entrySet()) {
	        if (!type.equals(entry.getValue().type())) {
	            result.put(entry.getKey(), entry.getValue());
	        }
	    }
	    return result;
	}
	
	private TimelineUtil() { Common.throwStaticClassInstantiateError(); }
}
