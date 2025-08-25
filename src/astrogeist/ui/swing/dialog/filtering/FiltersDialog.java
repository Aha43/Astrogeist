package astrogeist.ui.swing.dialog.filtering;

import java.awt.BorderLayout;

import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.filtering.FilteredTimelineViewTableModel;
import astrogeist.ui.swing.component.data.timeline.filtering.FiltersPanel;
import astrogeist.ui.swing.dialog.DialogBase;

public final class FiltersDialog extends DialogBase {
	private static final long serialVersionUID = 1L;

	private FiltersDialog(App app, FilteredTimelineViewTableModel model) {
		super(app, "Filters", false);
		var panel = new FiltersPanel(model);
		super.add(panel, BorderLayout.CENTER);
		super.pack();
		super.setSize(800, 600);
	}
	
	public static final void show(App app, FilteredTimelineViewTableModel model) {
		new FiltersDialog(app, model).setVisible(true); }
}
