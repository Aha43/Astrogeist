package astrogeist.ui.swing.component.data.timeline.view;

import javax.swing.JButton;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.filtering.FilteredTimelineViewStackDialog;

public final class CompositeFilteredTimelineViewTablePanel extends AbstractTimelineViewTablePanel {
	private static final long serialVersionUID = 1L;
	
	public CompositeFilteredTimelineViewTablePanel(App app) {
		super(app, new CompositeFilteredTimelineViewTableModel());
		createButtons();
	}
	
	private final CompositeFilteredTimelineViewTableModel model() { 
		return (CompositeFilteredTimelineViewTableModel)super.model; }
	
	private final void createButtons() {
		var model = model();
		
		
		var filters = new JButton("Filters");
		filters.addActionListener(e -> {
			FilteredTimelineViewStackDialog.show(app);
		});
		super.buttonsPanel.add(filters);
	}
	
	public final void timelineView(TimelineView baseView) {
		var model = this.model();
		model.timelineView(baseView);
		super.postSetData();
	}
	
}
