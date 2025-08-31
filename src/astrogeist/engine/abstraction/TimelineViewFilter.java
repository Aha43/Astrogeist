package astrogeist.engine.abstraction;

import java.time.Instant;

public interface TimelineViewFilter {
	String name();
	boolean accept(Instant time, TimelineView view);
}
