package astrogeist.engine.timeline;

import static astrogeist.Common.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import astrogeist.engine.issues.Issues;
import astrogeist.engine.scanner.NormalizedProperties;
import astrogeist.engine.typesystem.Type;

public class Timeline {
	private final LinkedHashMap<Instant, LinkedHashMap<String, TimelineValue>> timeline = new LinkedHashMap<>();
	
    public void put(Instant time, String key, TimelineValue tlv) {
    	time = requireNonNull(time, "time");
    	key = requireNonEmpty(key, "key");
    	
    	var normalizedKey = NormalizedProperties.getNormalized(key);
    	key = normalizedKey == null ? key : normalizedKey;
    	
        this.timeline.computeIfAbsent(time, t -> new LinkedHashMap<>()).put(key, tlv);
    }
    
    public void put(Instant time, LinkedHashMap<String, TimelineValue> values) {
    	for (var e : values.entrySet()) {
    		var key = NormalizedProperties.getNormalized(e.getKey());
    		if (key != null) put(time, key, e.getValue());
    	}
    }
    
    public String get(Instant time, String key) { 
    	var record = this.timeline.getOrDefault(time, new LinkedHashMap<>()).get(key);
    	return (record == null) ? null : record.value();
    }
    
    public LinkedHashMap<String, TimelineValue> snapshot(Instant time) {
        return this.timeline.getOrDefault(time, new LinkedHashMap<>()); }
    
    public List<TimelineValue> getOfType(Instant time, Type type) {
        return TimelineUtil.getOfType(timeline.getOrDefault(time, new LinkedHashMap<>()), type); }
    
    public Set<Instant> timestamps() { return new TreeSet<>(this.timeline.keySet()); }
    
    private Issues issues = new Issues();
    
    public Issues getIssues() { return this.issues; }
}
