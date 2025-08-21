package astrogeist.ui.swing.component.data.timeline.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.filtering.FilteredTimelineViewTableModel;

/**
 * <p>
 *   Panel that shows filters applied to time line and...
 * </p>
 */
public final class FiltersPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param app   Application.
	 * @param model The table model that shows the filtered time line and holds the filters. This does NOT
	 *              show this in a table but need reference to this in order to get the filters to show and
	 *              manipulate the filters applied.
	 */
	public FiltersPanel(App app, FilteredTimelineViewTableModel filters) {
		super.setLayout(new BorderLayout());
		
		var model = new FiltersTableModel(filters);
		
		var tabs = new JTabbedPane();
		
			var filterTab = new JScrollPane(new JTable(model));
			tabs.add("Filters", filterTab);
		
			tabs.add("Properties", new PropertiesFilterPanel(model));
			
		super.add(tabs, BorderLayout.CENTER);
	}	
}
