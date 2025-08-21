package astrogeist.engine.timeline.view;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.abstraction.TimelineViewFilter;
import astrogeist.engine.timeline.TimelineValue;

public final class CompositeFilteredTimelineView  implements TimelineView {
	private final ArrayList<TimelineView> views = new ArrayList<>();

	public void setBaseView(TimelineView baseView) {
		if (this.views.size() > 0) this.views.remove(0);
		this.views.addFirst(baseView);
	}
	
	@Override public final Map<String, TimelineValue> snapshot(Instant time) {
		if (this.views.size() == 0) return new HashMap<String, TimelineValue>();
		var last = this.views.getLast();
		return last == null ? new HashMap<String, TimelineValue>() : last.snapshot(time);
	}

	@Override public final NavigableSet<Instant> timestamps() {
		if (this.views.size() == 0) return new TreeSet<Instant>();
		var last = this.views.getLast();
		return last == null ? new TreeSet<Instant>() : last.timestamps();
	}
	
	public final int getFilterCount() {
		var n = this.views.size();
		return n == 0 ? 0 : n - 1; 
	}
	
	public final TimelineViewFilter getFilter(int pos) {
		if (pos < 1) {
			throw new IndexOutOfBoundsException("pos < 1 : " + pos);
		}
		
		int size = this.views.size();
		if (pos >= size) {
			throw new IndexOutOfBoundsException("pos >= " + size + " : " + pos);
		}
		
		var timelineView = (FilteredTimelineViewOld)this.views.get(pos);
		return timelineView.getFilter();
	}
	
	public final void clearFilters() {
		var base = this.views.getFirst();
		this.views.clear();
		this.views.add(base);
	}
	
	public final void deleteFilter(int pos) {
		if (pos < 1) {
			throw new IndexOutOfBoundsException("pos < 1 : " + pos);
		}
		
		int size = this.views.size();
		if (size == 0) {
			throw new IndexOutOfBoundsException("view empty");
		}
			
		if (pos >= size) {
			throw new IndexOutOfBoundsException("pos >= " + size + " : " + pos);
		}
		
		if (pos == size -1 ) {
			popFilter();
			return;
		}
		
		var base = this.views.get(pos - 1);
		this.views.remove(pos);
		var viewToRebase = this.views.get(pos);
		((FilteredTimelineViewOld)viewToRebase).rebase(base);
	}
	
	public final void addFilter(int pos, TimelineViewFilter filter) {
		if (pos < 1) {
			throw new IndexOutOfBoundsException("pos < 1 : " + pos);
		}
		
		int size = this.views.size();
		if (pos >= size) {
			throw new IndexOutOfBoundsException("pos >= " + size + " : " + pos);
		}
		
		if (pos == size - 1) {
			pushFilter(filter);
			return;
		}
		
		var baseView = this.views.get(pos - 1);
		var viewToRebase = this.views.get(pos);
		var newView = new FilteredTimelineViewOld(baseView, filter);
		this.views.add(newView);
		((FilteredTimelineViewOld)viewToRebase).rebase(newView);
	}
	
	public final void pushFilter(TimelineViewFilter filter) {
		var last = this.views.getLast();
		var newLast = new FilteredTimelineViewOld(last, filter);
		views.add(newLast);
	}
	
	public final void popFilter() {
		var n = this.views.size();
		if (n == 1) return; // throw?
		this.views.remove(n - 1);
	}

}
