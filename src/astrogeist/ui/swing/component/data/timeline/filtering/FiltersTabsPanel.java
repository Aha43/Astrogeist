package astrogeist.ui.swing.component.data.timeline.filtering;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import astrogeist.ui.swing.App;

/**
 * <p>
 *   Panel that shows filters applied to time line and...
 * </p>
 */
public final class FiltersTabsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param app   Application.
	 * @param ftvtm The table model that shows the filtered time line and holds the filters. This does NOT
	 *              show this in a table but need reference to this in order to get the filters to show and
	 *              manipulate the filters applied.
	 */
	public FiltersTabsPanel(App app, FilteredTimelineViewTableModel ftvtm) {
		super.setLayout(new BorderLayout());
		
		var filters = new FiltersTableModel(ftvtm);
		
		var tabs = new JTabbedPane();
		
			tabs.add("Filters", new FiltersListPanel(filters));
			tabs.add("Properties", new PropertiesFilterPanel(filters));
			
		super.add(tabs, BorderLayout.CENTER);
	}
	
}
