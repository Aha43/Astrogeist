package astrogeist.ui.swing.dialog.filtering;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.filtering.FilteredTimelineViewTableModel;
import astrogeist.ui.swing.component.data.timeline.filtering.FiltersTabsPanel;
import astrogeist.ui.swing.dialog.DialogBase;

public final class FiltersDialog extends DialogBase {
	private static final long serialVersionUID = 1L;

	private FiltersDialog(App app, FilteredTimelineViewTableModel model) {
		super(app, "Filters", false);
		
		var panel = new FiltersTabsPanel(app, model);
		
		super.add(panel, BorderLayout.CENTER);
		
		this.createButtons();
		
		super.pack();
		super.setSize(500, 500);
	}
	
	private final void createButtons() {
		var buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
			var close = new JButton("Close");
			close.addActionListener(e -> this.setVisible(false));
			buttons.add(close);
		super.add(buttons, BorderLayout.SOUTH);
	}
	
	public static final void show(App app, FilteredTimelineViewTableModel model) {
		new FiltersDialog(app, model).setVisible(true); }
}
