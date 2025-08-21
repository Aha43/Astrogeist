package astrogeist.ui.swing.component.data.timeline.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

import astrogeist.engine.logging.Log;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.filtering.FilteredTimelineViewTableModel;
import astrogeist.ui.swing.dialog.filtering.PropertiesTimelineFilterDialog;

/**
 * <p>
 *   Panel that shows filters applied to time line and...
 * </p>
 */
public final class FilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final Logger logger = Log.get(this);
	
	private final FilterTableModel model;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param app   Application.
	 * @param model The table model that shows the filtered time line and holds the filters. This does NOT
	 *              show this in a table but need reference to this in order to get the filters to show and
	 *              manipulate the filters applied.
	 */
	public FilterPanel(App app, FilteredTimelineViewTableModel filters) {
		super.setLayout(new BorderLayout());
		
		this.model = new FilterTableModel(filters);
		
		super.add(new JScrollPane(new JTable(this.model)), BorderLayout.CENTER);
		createButtons(app);
	}
	
	private final void createButtons(App app) {
		var buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		var create = new JButton("Create");
		create.addActionListener(e -> this.addPropertiesTimelineFilter(app));
		buttons.add(create);
		super.add(buttons, BorderLayout.SOUTH);
	}
	
	private final void addPropertiesTimelineFilter(App app) {
		var filter = PropertiesTimelineFilterDialog.show(app);
		model.pushFilter(filter);
		
		this.logger.info("added " + PropertiesTimelineFilterDialog.class.getSimpleName() + 
				" filter: " + filter + " and composite view now has " + 
				this.model.getFilterCount() + " filters");
	}
	
}
