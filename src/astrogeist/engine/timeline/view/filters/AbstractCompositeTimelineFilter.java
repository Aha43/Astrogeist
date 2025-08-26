package astrogeist.engine.timeline.view.filters;

import java.util.ArrayList;

import astrogeist.engine.abstraction.TimelineFilter;

public abstract class AbstractCompositeTimelineFilter extends AbstractTimelineFilter {
	private final ArrayList<TimelineFilter> filters = new ArrayList<>();
	
	public int size() { return this.filters.size(); }
	
	public TimelineFilter get(int idx) { return this.filters.get(idx); }
	
	public AbstractCompositeTimelineFilter add(TimelineFilter filter) {
		this.filters.add(filter); return this; }
}
