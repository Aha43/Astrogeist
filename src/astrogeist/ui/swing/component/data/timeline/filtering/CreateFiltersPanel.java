package astrogeist.ui.swing.component.data.timeline.filtering;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import astrogeist.ui.swing.component.data.timeline.filtering.time.TimeFilterPanel;

/**
 * <p>
 *   Panel with tabs for specialized panels to create filters.
 * </p>
 */
public final class CreateFiltersPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * 
	 */
	public CreateFiltersPanel(FiltersTableModel ftm) { 
		super.setLayout(new BorderLayout());
		
		var north = new JPanel(new FlowLayout(FlowLayout.CENTER));
		north.add(new JLabel("Create Filters"));
		super.add(north, BorderLayout.NORTH);
		
		var center = new JTabbedPane();
		this.populateTabs(center, ftm);
		super.add(center, BorderLayout.CENTER);
	}
	
	private final void populateTabs(JTabbedPane tabs, FiltersTableModel ftm) {
		tabs.add("Time", new TimeFilterPanel(ftm));
		tabs.add("Properties", new PropertiesFilterPanel(ftm));
	}
	
}
