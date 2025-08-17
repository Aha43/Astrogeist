package astrogeist.ui.swing.dialog.filtering;

import java.awt.BorderLayout;

import astrogeist.engine.timeline.view.CompositeFilteredTimelineView;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.view.FilteredTimelineViewStackPanel;
import astrogeist.ui.swing.dialog.DialogBase;

public final class FilteredTimelineViewStackDialog extends DialogBase {
	private static final long serialVersionUID = 1L;
	
	private final FilteredTimelineViewStackPanel panel;

	private FilteredTimelineViewStackDialog(App app) {
		super(app, "Filters", false);
		
		this.panel = new FilteredTimelineViewStackPanel(app);
		
		super.add(this.panel, BorderLayout.CENTER);
		super.pack();
		super.setSize(500, 500);
	}
	
	public static final void show(App app) {
		new FilteredTimelineViewStackDialog(app).setVisible(true); }
}
