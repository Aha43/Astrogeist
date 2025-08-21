package astrogeist.engine.timeline.view;

import astrogeist.engine.abstraction.TimelineViewFilter;

/**
 * <p>
 *   Base class for time line filter implementations.
 * </p>
 */
public abstract class AbstractTimelineViewFilter implements TimelineViewFilter {
	@Override public final String name() { return this.getClass().getSimpleName(); }
}
