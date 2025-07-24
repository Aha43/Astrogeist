package astrogeist.app.component;

import javax.swing.*;
import java.awt.*;
import astrogeist.store.ObservationStore;

public class ObservationTablePanel extends JPanel {
	private final JTable table;
	private final ObservationTableModel tableModel;

	public ObservationTablePanel() {
		super(new BorderLayout());
		tableModel = new ObservationTableModel();
		table = new JTable(tableModel);

		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JScrollPane scrollPane = new JScrollPane(table);
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
