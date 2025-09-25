package astrogeist.engine.timeline.view.filters;

import java.util.ArrayList;

import astrogeist.engine.abstraction.timeline.TimelineViewFilter;

public abstract class AbstractCompositeTimelineViewFilter extends AbstractTimelineViewFilter {
	private final ArrayList<TimelineViewFilter> filters = new ArrayList<>();
	
	protected AbstractCompositeTimelineViewFilter() { }
	protected AbstractCompositeTimelineViewFilter(String description) { this.description(description); }
	
	private String description = "";
	public void description(String description) {
		description = description == null ? "" : description.trim();
		this.description = description; 
	}
	public String description() { return this.description; }
	
	public int size() { return this.filters.size(); }
	
	public TimelineViewFilter get(int idx) { return this.filters.get(idx); }
	
	public AbstractCompositeTimelineViewFilter add(TimelineViewFilter filter) {
		this.filters.add(filter); return this; }
}
