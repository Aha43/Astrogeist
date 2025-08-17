package astrogeist.engine.timeline.view;

import astrogeist.engine.abstraction.TimelineViewFilter;

public abstract class AbstractTimelineViewFilter implements TimelineViewFilter {
	@Override public final String name() { return this.getClass().getSimpleName(); }
}
