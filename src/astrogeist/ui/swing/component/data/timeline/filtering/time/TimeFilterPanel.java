package astrogeist.ui.swing.component.data.timeline.filtering.time;

import javax.swing.JTabbedPane;

import astrogeist.ui.swing.component.data.timeline.filtering.FiltersTableModel;

public final class TimeFilterPanel extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	
	public TimeFilterPanel(FiltersTableModel ftm) { this.populateTabs(ftm); }
	
	private final void populateTabs(FiltersTableModel ftm) {
		super.add("Day", new CreateDayFilterPanel(ftm));
	}

}
