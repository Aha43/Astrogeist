package astrogeist.ui.swing.component.data.timeline.filtering;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public final class FiltersListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public FiltersListPanel(FiltersTableModel filters) {
		super.setLayout(new BorderLayout());
		
		var table = new JTable(filters);
		var scroll = new JScrollPane(table);
		super.add(scroll, BorderLayout.CENTER);
		
		var buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		var clear = new JButton("Clear");
		clear.addActionListener(e -> filters.clearFilters());
		buttons.add(clear);
		super.add(buttons, BorderLayout.SOUTH);
	}

}
