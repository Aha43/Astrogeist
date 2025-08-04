package astrogeist.timeline.mapping;

import java.util.LinkedHashMap;
import java.util.Set;

import astrogeist.Common;

public final class TimelineMap {
	private static final LinkedHashMap<String, String> _mapping = new LinkedHashMap<>();
	
	private TimelineMap() { Common.throwStaticClassInstantiateError(); }
	
	public static void Add(String normalized, String ...synonyms) {
		_mapping.put(normalized, normalized);
		if (synonyms != null)
			for (var s : synonyms) { _mapping.put(s, normalized); }
	}
	
	public static String getTimelineName(String key) { return _mapping.get(key); }
	
	public static Set<String> getTimelineNames() { return _mapping.keySet(); }
}
