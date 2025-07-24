package astrogeist.app.component;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import astrogeist.store.ObservationStore;

public final class ObservationTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTable table;
	private final ObservationTableModel tableModel;

	public ObservationTablePanel() {
		super(new BorderLayout());
		tableModel = new ObservationTableModel();
		table = new JTable(tableModel);

		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(table);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public void setStore(ObservationStore store) {
		tableModel.setStore(store);
	}

	public JTable getTable() {
		return table;
	}

	public ObservationTableModel getTableModel() {
		return tableModel;
	}

}
