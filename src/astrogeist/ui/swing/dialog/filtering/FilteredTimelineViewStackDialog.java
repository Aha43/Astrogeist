package astrogeist.ui.swing.dialog.filtering;

import java.awt.BorderLayout;

import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.filtering.FilteredTimelineViewTableModel;
import astrogeist.ui.swing.component.data.timeline.view.FilterPanel;
import astrogeist.ui.swing.dialog.DialogBase;

public final class FilteredTimelineViewStackDialog extends DialogBase {
	private static final long serialVersionUID = 1L;
	
	private final FilterPanel panel;

	private FilteredTimelineViewStackDialog(App app, FilteredTimelineViewTableModel model) {
		super(app, "Filters", false);
		
		this.panel = new FilterPanel(app, model);
		
		super.add(this.panel, BorderLayout.CENTER);
		super.pack();
		super.setSize(500, 500);
	}
	
	public static final void show(App app, FilteredTimelineViewTableModel model) {
		new FilteredTimelineViewStackDialog(app, model).setVisible(true); }
}
