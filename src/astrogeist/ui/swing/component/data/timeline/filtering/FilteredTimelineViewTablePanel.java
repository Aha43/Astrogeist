package astrogeist.ui.swing.component.data.timeline.filtering;

import javax.swing.JButton;
import javax.swing.JTable;

import aha.common.abstraction.io.appdata.AppDataManager;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.engine.abstraction.timeline.TimelineView;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.view.AbstractTimelineViewTablePanel;
import astrogeist.ui.swing.dialog.filtering.FiltersDialog;

/**
 * <p>
 *   Panel that show filtered time line in a 
 *   {@link JTable}.
 * </p>
 */
public final class FilteredTimelineViewTablePanel 
	extends AbstractTimelineViewTablePanel {
	
	private static final long serialVersionUID = 1L;
	
	public FilteredTimelineViewTablePanel(
		App app,
		AppDataManager astrogeistStorageManager,
		TimelineNames timelineNames,
		SnapshotSelectionService sss) { 
		super(app, astrogeistStorageManager,
			new FilteredTimelineViewTableModel(), timelineNames, sss);
		this.createButtons();
	}
	
	private final FilteredTimelineViewTableModel model() { 
		return (FilteredTimelineViewTableModel)super.model; }
	
	private final void createButtons() {
		var model = model();
		var filters = new JButton("Filters");
		filters.addActionListener(e -> { FiltersDialog.show(app, model); });
		super.northPanel.add(filters);
	}
	
	public final void timelineView(TimelineView view) {
		var model = this.model();
		model.setTimeline(view);
		super.postSetData();
	}

}
