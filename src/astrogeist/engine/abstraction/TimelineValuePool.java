package astrogeist.engine.abstraction;

import java.nio.file.Path;

import astrogeist.engine.timeline.TimelineValue;

public interface TimelineValuePool {
	TimelineValue get(String name, String value);
	TimelineValue getFileValue(Path path);
}
