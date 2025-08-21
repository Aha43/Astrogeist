package astrogeist.ui.swing.component.data.timeline.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import astrogeist.engine.timeline.view.PropertiesTimelineViewFilter;
import astrogeist.ui.swing.component.general.KeyValuePairsPanel;

public final class PropertiesFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final KeyValuePairsPanel kvpp = new KeyValuePairsPanel();
	
	public PropertiesFilterPanel(FiltersTableModel ftm) {
		super.setLayout(new BorderLayout());
		super.add(this.kvpp, BorderLayout.CENTER);
		
		var buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
			var create = new JButton("Create");
			create.addActionListener(e -> {
				var result = this.kvpp.getPairs();
				var filter = new PropertiesTimelineViewFilter(result);
				ftm.pushFilter(filter);
			});
			buttons.add(create);
		super.add(buttons, BorderLayout.SOUTH);
	}

}
