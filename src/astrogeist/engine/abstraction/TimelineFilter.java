package astrogeist.engine.abstraction;

import java.time.Instant;

public interface TimelineFilter {
	String name();
	boolean accept(Instant time, TimelineView view);
}
