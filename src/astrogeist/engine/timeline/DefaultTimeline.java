package astrogeist.engine.timeline;

import static astrogeist.Common.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.TimelineValuePool;
import astrogeist.engine.issues.Issues;
import astrogeist.engine.scanner.NormalizedProperties;
import astrogeist.engine.typesystem.Type;

public final class DefaultTimeline implements Timeline {
	private final TimelineValuePool timelineValuePool;
	
	private final LinkedHashMap<Instant, LinkedHashMap<String, TimelineValue>> timeline = new LinkedHashMap<>();
	
	public DefaultTimeline(TimelineValuePool timelineValuePool) { this.timelineValuePool = timelineValuePool; }
	
	public void clear() { this.timeline.clear(); }
	
	public void put(Instant time, Path path) {
		var fileName = path.getFileName().toString();
		var tlv = this.timelineValuePool.get("file", path.toString());
		this.timeline.computeIfAbsent(time, t -> new LinkedHashMap<>()).put(fileName, tlv);
	}
	
	public void put(Instant time, LinkedHashMap<String, String> values) {
		var snapshot = this.timeline.get(time);
		if (snapshot == null) {
			snapshot = new LinkedHashMap<>();
			this.timeline.put(time, snapshot);
		}
		
		for (var e : values.entrySet()) {
			var key = e.getKey();
			var normalizedKey = NormalizedProperties.getNormalized(key);
	    	key = normalizedKey == null ? key : normalizedKey;
	    	var tlv = this.timelineValuePool.get(key, e.getValue());
	    	snapshot.put(key, tlv);
		}
	}
	
    public void put(Instant time, String key, String value) {
    	time = requireNonNull(time, "time");
    	key = requireNonEmpty(key, "key");
    	
    	var normalizedKey = NormalizedProperties.getNormalized(key);
    	key = normalizedKey == null ? key : normalizedKey;
    	
    	var tlv = this.timelineValuePool.get(key, value);
        this.timeline.computeIfAbsent(time, t -> new LinkedHashMap<>()).put(key, tlv);
    }
    
    public void put(Instant)
    
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
