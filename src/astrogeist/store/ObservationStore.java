package astrogeist.store;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ObservationStore {
	private final Map<Instant, Map<String, String>> store = new HashMap<>();

    public void put(Instant time, String key, String value) {
        store.computeIfAbsent(time, t -> new HashMap<>()).put(key, value);
    }

    public String get(Instant time, String key) {
        return store.getOrDefault(time, Map.of()).get(key);
    }

    public Map<String, String> snapshot(Instant time) {
        return store.getOrDefault(time, Map.of());
    }
    
    public Set<Instant> timestamps() {
        // Return a sorted set of all timestamps
        return new TreeSet<>(store.keySet());
    }
    
    public static ObservationStore createDummyStore() {
        ObservationStore store = new ObservationStore();

        for (int i = 0; i < 5; i++) {
            Instant timestamp = Instant.now().minusSeconds(i * 3600); // 1 hour apart

            store.put(timestamp, "subject", "Sun");
            store.put(timestamp, "movie:exposure", String.valueOf(10 + i));
            store.put(timestamp, "scope", "Lunt 60MT");
        }

        return store;
    }

}
