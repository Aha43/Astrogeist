package astrogeist.ui.swing.component.data.timeline.filtering;

import javax.swing.JButton;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.view.AbstractTimelineViewTablePanel;
import astrogeist.ui.swing.dialog.filtering.FiltersDialog;

/**
 * <p>
 *   Panel that show filtered time line in a JTable.
 * </p>
 */
public class FilteredTimelineViewTablePanel extends AbstractTimelineViewTablePanel {
	private static final long serialVersionUID = 1L;
	
	public FilteredTimelineViewTablePanel(App app) { 
		super(app, new FilteredTimelineViewTableModel());
		this.createButtons();
	}
	
	private final FilteredTimelineViewTableModel model() { 
		return (FilteredTimelineViewTableModel)super.model; }
	
	private final void createButtons() {
		var model = model();
		var filters = new JButton("Filters");
		filters.addActionListener(e -> { FiltersDialog.show(app, model); });
		super.buttonsPanel.add(filters);
	}
	
	public final void timelineView(TimelineView view) {
		var model = this.model();
		model.setTimeline(view);
		super.postSetData();
	}

}
