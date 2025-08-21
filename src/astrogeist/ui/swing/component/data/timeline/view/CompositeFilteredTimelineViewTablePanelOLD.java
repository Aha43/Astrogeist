package astrogeist.ui.swing.component.data.timeline.view;

import javax.swing.JButton;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.filtering.FilteredTimelineViewStackDialog;

public final class CompositeFilteredTimelineViewTablePanelOLD extends AbstractTimelineViewTablePanel {
	private static final long serialVersionUID = 1L;
	
	public CompositeFilteredTimelineViewTablePanelOLD(App app) {
		super(app, new CompositeFilteredTimelineViewTableModelOld());
		createButtons();
	}
	
	private final CompositeFilteredTimelineViewTableModelOld model() { 
		return (CompositeFilteredTimelineViewTableModelOld)super.model; }
	
	private final void createButtons() {
		var model = model();
		
		
		var filters = new JButton("Filters");
		filters.addActionListener(e -> {
			//FilteredTimelineViewStackDialog.show(app, model);
		});
		super.buttonsPanel.add(filters);
	}
	
	public final void timelineView(TimelineView baseView) {
		var model = this.model();
		model.timelineView(baseView);
		super.postSetData();
	}
	
}
