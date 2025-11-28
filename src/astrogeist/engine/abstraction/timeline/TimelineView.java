package astrogeist.engine.abstraction.timeline;

import java.time.Instant;
import java.util.NavigableSet;

import astrogeist.engine.timeline.Snapshot;

public interface TimelineView {
	Snapshot snapshot(Instant time);
	NavigableSet<Instant> timestamps();
}
