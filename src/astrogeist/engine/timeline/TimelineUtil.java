package astrogeist.engine.timeline;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import astrogeist.Common;
import astrogeist.engine.typesystem.Type;

public final class TimelineUtil {
	public static List<TimelineValue> getOfType(Map<String, TimelineValue> data, Type type){
		return data.values().stream().filter(v -> Type.isA(v.type(), type)).collect(Collectors.toList()); }
	
	public static Map<String, TimelineValue> getExcludingTypeMap(
	    Map<String, TimelineValue> data, Type type) {
	    
	    Map<String, TimelineValue> result = new LinkedHashMap<>();
	    for (Entry<String, TimelineValue> entry : data.entrySet()) {
	    	if (!Type.isA(entry.getValue().type(), type))
	            result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}
	
	private TimelineUtil() { Common.throwStaticClassInstantiateError(); }
}
