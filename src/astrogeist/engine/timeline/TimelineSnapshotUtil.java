package astrogeist.engine.timeline;

import static aha.common.util.Guards.throwStaticClassInstantiateError;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.typesystem.Type;

/**
 * <p>
 *   Utility methods of use when working with snapshot of a
 *   {@link Timeline}.   
 * </p>
 */
public final class TimelineSnapshotUtil {
	private TimelineSnapshotUtil() { throwStaticClassInstantiateError(); }
	
	public final static List<TimelineValue> getOfType(
		Map<String, TimelineValue> data, Type type){
		
		var retVal = data.values().stream().filter(
			v -> Type.isA(v.type(), type)).collect(Collectors.toList()); 
		return retVal;
	}
	
	public final static Map<String, TimelineValue> getExcludingTypeMap(
	    Map<String, TimelineValue> data, Type type) {
	    
	    Map<String, TimelineValue> result = new LinkedHashMap<>();
	    for (Entry<String, TimelineValue> entry : data.entrySet()) {
	    	if (!Type.isA(entry.getValue().type(), type))
	            result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}
	
}
