package astrogeist.store;

import static astrogeist.Common.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import astrogeist.scanner.NormalizedProperties;

public class ObservationStore {
	private final Map<Instant, Map<String, String>> store = new HashMap<>();

    public void put(Instant time, String key, String value) {
    	time = requireNonNull(time, "time");
    	key = requireNonEmpty(key, "key");
    	
    	value = value == null ? "" : value.trim();
    	
    	var normalizedKey = NormalizedProperties.getNormalized(key);
    	key = normalizedKey == null ? key : normalizedKey;
    	
        this.store.computeIfAbsent(time, t -> new HashMap<>()).put(key, value);
    }
    
    public void put(Instant time, Map<String, String> values) {
    	for (var e : values.entrySet()) {
    		var key = NormalizedProperties.getNormalized(e.getKey());
    		if (key != null) put(time, key, e.getValue());
    	}
    }

    public String get(Instant time, String key) { return this.store.getOrDefault(time, Map.of()).get(key); }
    public Map<String, String> snapshot(Instant time) { return this.store.getOrDefault(time, Map.of()); }
    public Set<Instant> timestamps() { return new TreeSet<>(this.store.keySet()); }
}
