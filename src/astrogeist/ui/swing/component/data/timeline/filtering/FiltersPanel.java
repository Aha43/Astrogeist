package astrogeist.ui.swing.component.data.timeline.filtering;

import javax.swing.JSplitPane;

public final class FiltersPanel extends JSplitPane {
	private static final long serialVersionUID = 1L;
	
	public FiltersPanel(FilteredTimelineViewTableModel ftvtm) {
		var ftm = new FiltersTableModel(ftvtm);
		
		super.setDividerLocation(200);
		
		var left = new FiltersListPanel(ftm);
		var right = new CreateFiltersPanel(ftm);
		super.setLeftComponent(left);
		super.setRightComponent(right);
	}
}
