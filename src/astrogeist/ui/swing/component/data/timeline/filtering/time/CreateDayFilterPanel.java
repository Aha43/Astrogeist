package astrogeist.ui.swing.component.data.timeline.filtering.time;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import astrogeist.common.Instants;
import astrogeist.engine.timeline.view.filters.TimeRangeTimelineViewFilter;
import astrogeist.ui.swing.component.data.timeline.filtering.FiltersTableModel;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class CreateDayFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public CreateDayFilterPanel(FiltersTableModel ftm) {
		setLayout(new BorderLayout());
		
		var north = new JPanel(new BorderLayout(5, 0));
		north.add(new JLabel("yyyy-mm-dd: "), BorderLayout.WEST);
		var text = new JTextField();
		var defaultTimeText = Instants.todayIsoUtc();
		text.setText(defaultTimeText);
		text.setMaximumSize(new Dimension(Integer.MAX_VALUE, text.getPreferredSize().height));
		north.add(text, BorderLayout.CENTER);
		super.add(north, BorderLayout.NORTH);
		
		var south = new JPanel(new FlowLayout(FlowLayout.CENTER));
		var create = new JButton("Create");
		create.addActionListener(e -> createFilter(ftm, text.getText()));
		south.add(create);
		super.add(south, BorderLayout.SOUTH);
	}
	
	private final void createFilter(FiltersTableModel ftm, String isoDate) {
		if (!Instants.isIsoDate(isoDate)) {
			MessageDialogs.showInfo(this, "'" + isoDate + "' is not in valid format, use: 'yyyy-mm-dd'");
			return;
		}
		var interval = Instants.dayInterval(isoDate);
		var filter = new TimeRangeTimelineViewFilter(interval);
		ftm.pushFilter(filter);
	}

}
