package astrogeist.store;

import static astrogeist.Common.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeSet;

import astrogeist.scanner.NormalizedProperties;

public class ObservationStore {
	private final LinkedHashMap<Instant, LinkedHashMap<String, String>> store = new LinkedHashMap<>();

    public void put(Instant time, String key, String value) {
    	time = requireNonNull(time, "time");
    	key = requireNonEmpty(key, "key");
    	
    	value = value == null ? "" : value.trim();
    	
    	var normalizedKey = NormalizedProperties.getNormalized(key);
    	key = normalizedKey == null ? key : normalizedKey;
    	
        this.store.computeIfAbsent(time, t -> new LinkedHashMap<>()).put(key, value);
    }
    
    public void put(Instant time, LinkedHashMap<String, String> values) {
    	for (var e : values.entrySet()) {
    		var key = NormalizedProperties.getNormalized(e.getKey());
    		if (key != null) put(time, key, e.getValue());
    	}
    }
    
    public String get(Instant time, String key) { 
    	return this.store.getOrDefault(time, new LinkedHashMap<>()).get(key); }
    
    
    public LinkedHashMap<String, String> snapshot(Instant time) { 
    	return this.store.getOrDefault(time, new LinkedHashMap<>()); }
    
    public Set<Instant> timestamps() { return new TreeSet<>(this.store.keySet()); }
}
