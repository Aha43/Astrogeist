package astrogeist.engine.abstraction;

import astrogeist.engine.timeline.TimelineValue;

public interface TimelineValuePool {
	TimelineValue get(String name, String value);
}
