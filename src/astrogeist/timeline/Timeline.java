package astrogeist.timeline;

import static astrogeist.Common.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import astrogeist.issues.Issues;
import astrogeist.scanner.NormalizedProperties;
import astrogeist.typesystem.Type;

public class Timeline {
	private final LinkedHashMap<Instant, LinkedHashMap<String, TimelineValue>> timeline = new LinkedHashMap<>();

	public void put(Instant time, String key, String value) { this.put(time, key, value, null); }
	
    public void put(Instant time, String key, String value, Type type) {
    	time = requireNonNull(time, "time");
    	key = requireNonEmpty(key, "key");
    	
    	value = value == null ? "" : value.trim();
    	
    	var normalizedKey = NormalizedProperties.getNormalized(key);
    	key = normalizedKey == null ? key : normalizedKey;
    	
        this.timeline.computeIfAbsent(time, t -> new LinkedHashMap<>()).put(key, new TimelineValue(value, type));
    }
    
    public void put(Instant time, LinkedHashMap<String, String> values) {
    	for (var e : values.entrySet()) {
    		var key = NormalizedProperties.getNormalized(e.getKey());
    		if (key != null) put(time, key, e.getValue());
    	}
    }
    
    public String get(Instant time, String key) { 
    	var record = this.timeline.getOrDefault(time, new LinkedHashMap<>()).get(key);
    	return (record == null) ? null : record.value();
    }
    
    public LinkedHashMap<String, String> snapshot(Instant time) {
        LinkedHashMap<String, TimelineValue> row = this.timeline.get(time);
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        if (row != null) {
            for (Map.Entry<String, TimelineValue> entry : row.entrySet()) {
                result.put(entry.getKey(), entry.getValue().value());
            }
        }
        return result;
    }
    
    public LinkedHashMap<String, TimelineValue> snapshotRaw(Instant time) {
        return this.timeline.getOrDefault(time, new LinkedHashMap<>()); }
    
    public List<TimelineValue> getOfType(Instant time, Type type) {
        return TimelineUtil.getOfType(timeline.getOrDefault(time, new LinkedHashMap<>()), type); }
    
    public Set<Instant> timestamps() { return new TreeSet<>(this.timeline.keySet()); }
    
    private Issues issues = new Issues();
    
    public Issues getIssues() { return this.issues; }
}
