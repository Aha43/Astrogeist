package astrogeist.timeline;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import astrogeist.Common;

public final class TimelineUtil {

	public static List<TimelineValue> getOfType(LinkedHashMap<String, TimelineValue> data, String type){
		return data.values().stream().filter(v -> type.equals(v.type())).collect(Collectors.toList());
	}
	
	private TimelineUtil() { Common.throwStaticClassInstantiateError(); }
}
