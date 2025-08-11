package astrogeist.engine.abstraction;

import java.time.Instant;
import java.util.Map;

import astrogeist.engine.timeline.TimelineValue;

public interface TimelineSnapshotFilter {
    boolean accepts(Instant time, Map<String, TimelineValue> snapshot);
    String label(); // user-facing description for UI
    default String id() { return getClass().getName() + ":" + label(); } // stable identity for equals/hashCode if needed
}
