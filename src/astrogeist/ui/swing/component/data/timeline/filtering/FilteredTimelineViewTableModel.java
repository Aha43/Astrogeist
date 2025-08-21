package astrogeist.ui.swing.component.data.timeline.filtering;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.abstraction.TimelineViewFilter;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.timeline.view.FilteredTimelineView;
import astrogeist.ui.swing.component.data.timeline.view.AbstractTimelineViewTableModel;

/**
 * <p>
 *   Table model showing filtered time line.
 * </p>
 */
public final class FilteredTimelineViewTableModel  extends AbstractTimelineViewTableModel {
	private static final long serialVersionUID = 1L;
	
	private TimelineView unfilteredView;
	
	private FilteredTimelineView filteredView = new FilteredTimelineView();
	
	private final ArrayList<TimelineViewFilter> filters = new ArrayList<>();
	
	public FilteredTimelineViewTableModel() {
		//var filter = new astrogeist.engine.timeline.view.PropertyEqualsTimelineViewFilter("FrameCount", "1000");
		//this.pushFilter(filter);
	}

	@Override protected TimelineView getTimelineView() { return this.filteredView; }
	
	public final void setTimeline(TimelineView view) {
		this.unfilteredView = view;
		this.filter();
	}
	
	public final int getFilterCount() { return this.filters.size(); }
	
	public final TimelineViewFilter getFilter(int idx) { return this.filters.get(idx); }
	
	public final void pushFilter(TimelineViewFilter filter) {
		this.filters.add(filter);
		filter();
	}
	
	private final void filter() {
		if (this.unfilteredView == null) return;
		
		ConcurrentNavigableMap<Instant, ConcurrentNavigableMap<String, TimelineValue>> filtered =
				new ConcurrentSkipListMap<>();
		
		for (var time : this.unfilteredView.timestamps()) {
			if (accept(time)) {
				var snapshot = this.unfilteredView.snapshot(time);
				filtered.put(time, new ConcurrentSkipListMap<>(snapshot));
			}
		}
		
		this.filteredView = new FilteredTimelineView(filtered);
		
		super.initialize();
	}
	
	private boolean accept(Instant time) {
		for (var filter : this.filters)
			if (!filter.accept(time, this.unfilteredView)) return false;
		return true;
	}

}
