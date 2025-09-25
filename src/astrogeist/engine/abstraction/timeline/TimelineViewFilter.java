package astrogeist.engine.abstraction.timeline;

import java.time.Instant;

public interface TimelineViewFilter {
	String name();
	boolean accept(Instant time, TimelineView view);
}
