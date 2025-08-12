package astrogeist.ui.swing.component.data.timeline.view;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.ui.swing.App;

public final class CompositeFilteredTimelineViewTablePanel extends AbstractTimelineViewTablePanel {
	private static final long serialVersionUID = 1L;
	
	public CompositeFilteredTimelineViewTablePanel(App app) {
		super(app, new CompositeFilteredTimelineViewTableModel()); }
	
	public final void setTimelineView(TimelineView data) {
		var model = (CompositeFilteredTimelineViewTableModel)super.tableModel;
		model.setTimelineView(data); 
		super.postSetData();
	}
	
	public final CompositeFilteredTimelineViewTableModel getTableModel() { 
		return (CompositeFilteredTimelineViewTableModel)this.tableModel; }
}
