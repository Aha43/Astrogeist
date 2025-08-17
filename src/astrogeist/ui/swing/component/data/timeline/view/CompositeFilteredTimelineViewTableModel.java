package astrogeist.ui.swing.component.data.timeline.view;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.timeline.view.CompositeFilteredTimelineView;

public final class CompositeFilteredTimelineViewTableModel extends AbstractTimelineViewTableModel {
	private static final long serialVersionUID = 1L;
	
	private final CompositeFilteredTimelineView cftlv = new CompositeFilteredTimelineView();

	protected void timelineView(TimelineView view) { 
		this.cftlv.setBaseView(view);
		initialize(view);
	}
	
	@Override public TimelineView getTimelineView() { return this.cftlv; }
}
