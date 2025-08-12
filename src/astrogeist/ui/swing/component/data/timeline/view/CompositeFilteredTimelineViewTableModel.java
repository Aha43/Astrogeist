package astrogeist.ui.swing.component.data.timeline.view;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.timeline.view.CompositeFilteredTimelineView;

public final class CompositeFilteredTimelineViewTableModel extends AbstractTimelineViewTableModel {
	private static final long serialVersionUID = 1L;
	
	public final void setTimelineView(TimelineView view) {
		super.setView(new CompositeFilteredTimelineView(view)); }
}
