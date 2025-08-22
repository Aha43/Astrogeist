package astrogeist.ui.swing.component.data.timeline.filtering;

import javax.swing.JButton;

import astrogeist.engine.timeline.view.PropertiesTimelineViewFilter;
import astrogeist.ui.swing.component.general.KeyValuePairsPanel;

public final class PropertiesFilterPanel extends KeyValuePairsPanel {
	private static final long serialVersionUID = 1L;
	
	public PropertiesFilterPanel(FiltersTableModel ftm) {
		var create = new JButton("Create");
		create.addActionListener(e -> {
			var result = this.getPairs();
			var filter = new PropertiesTimelineViewFilter(result);
			ftm.pushFilter(filter);
		});
		super.addButton(create);
	}
}
