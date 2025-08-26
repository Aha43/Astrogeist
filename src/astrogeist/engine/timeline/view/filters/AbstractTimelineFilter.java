package astrogeist.engine.timeline.view.filters;

import astrogeist.engine.abstraction.TimelineFilter;

/**
 * <p>
 *   Base class for time line filter implementations.
 * </p>
 */
public abstract class AbstractTimelineFilter implements TimelineFilter {
	@Override public final String name() { return this.getClass().getSimpleName(); }
}
