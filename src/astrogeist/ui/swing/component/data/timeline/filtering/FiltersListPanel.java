package astrogeist.ui.swing.component.data.timeline.filtering;

import java.awt.BorderLayout;

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
	}

}
