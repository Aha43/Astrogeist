package astrogeist.ui.swing.component.data.timeline.view;

import java.util.logging.Logger;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.abstraction.TimelineViewFilter;
import astrogeist.engine.logging.Log;
import astrogeist.engine.timeline.view.CompositeFilteredTimelineView;

public final class CompositeFilteredTimelineViewTableModelOld extends AbstractTimelineViewTableModel {
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Log.get(this);
	
	private final CompositeFilteredTimelineView cftlv = new CompositeFilteredTimelineView();

	protected void timelineView(TimelineView view) { 
		this.cftlv.setBaseView(view);
		initialize();
	}
	
	@Override public TimelineView getTimelineView() { return this.cftlv; }
	
	public final int getFilterCount() { return this.cftlv.getFilterCount(); }
	
	public final TimelineViewFilter getFilter(int pos) { return this.cftlv.getFilter(pos); }
	
	public final void pushFilter(TimelineViewFilter filter) {
		logger.info("Push filter: " + filter.toString());
		this.cftlv.pushFilter(filter);
		super.fireTableDataChanged();
	}
}
