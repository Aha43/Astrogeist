package astrogeist.engine.abstraction;

import java.time.Instant;

public interface TimelineViewFilter {
	boolean accept(Instant time, TimelineView view);
}
