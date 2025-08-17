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
import astrogeist.ui.swing.dialog.filtering.PropertiesTimelineFilterDialog;

public final class FilteredTimelineViewStackPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final Logger logger = Log.get(this);
	
	private final FilteredTimelineViewTableModel model = new FilteredTimelineViewTableModel(); 
	
	public FilteredTimelineViewStackPanel(App app) {
		super.setLayout(new BorderLayout());
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
		model.addFilter(filter);
		
		this.logger.info("added " + PropertiesTimelineFilterDialog.class.getSimpleName() + 
				" filter: " + filter + " and composite view now has " + 
				this.model.view().getFilterCount() + " filters");
	}
	
	
}
