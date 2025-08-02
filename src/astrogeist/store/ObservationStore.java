package astrogeist.store;

import static astrogeist.Common.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import astrogeist.scanner.NormalizedProperties;
import astrogeist.util.Strings;

public class ObservationStore {
	private final LinkedHashMap<Instant, LinkedHashMap<String, TimelineValue>> store = new LinkedHashMap<>();

	public void put(Instant time, String key, String value) { this.put(time, key, value, null); }
	
    public void put(Instant time, String key, String value, String type) {
    	time = requireNonNull(time, "time");
    	key = requireNonEmpty(key, "key");
    	
    	type = Strings.isNullOrBlank(type) ? "string" : type;
    	
    	value = value == null ? "" : value.trim();
    	
    	var normalizedKey = NormalizedProperties.getNormalized(key);
    	key = normalizedKey == null ? key : normalizedKey;
    	
        this.store.computeIfAbsent(time, t -> new LinkedHashMap<>()).put(key, new TimelineValue(value, type));
    }
    
    public void put(Instant time, LinkedHashMap<String, String> values) {
    	for (var e : values.entrySet()) {
    		var key = NormalizedProperties.getNormalized(e.getKey());
    		if (key != null) put(time, key, e.getValue());
    	}
    }
    
    public String get(Instant time, String key) { 
    	var record = this.store.getOrDefault(time, new LinkedHashMap<>()).get(key);
    	return (record == null) ? null : record.value();
    }
    
    
    public LinkedHashMap<String, String> snapshot(Instant time) {
        LinkedHashMap<String, TimelineValue> row = this.store.get(time);
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        if (row != null) {
            for (Map.Entry<String, TimelineValue> entry : row.entrySet()) {
                result.put(entry.getKey(), entry.getValue().value());
            }
        }
        return result;
    }
    
    public LinkedHashMap<String, TimelineValue> snapshotRaw(Instant time) {
        return this.store.getOrDefault(time, new LinkedHashMap<>());
    }
    
    public List<TimelineValue> getOfType(Instant time, String type) {
        return store.getOrDefault(time, new LinkedHashMap<>()).values().stream()
            .filter(v -> type.equals(v.type()))
            .collect(Collectors.toList());
    }
    
    public Set<Instant> timestamps() { return new TreeSet<>(this.store.keySet()); }
}
