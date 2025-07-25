package astrogeist.app.component;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;

import astrogeist.store.ObservationStore;

public final class ObservationTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTable _table;
	private final ObservationTableModel _tableModel;

	public ObservationTablePanel() {
		super(new BorderLayout());
		_tableModel = new ObservationTableModel();
		_table = new JTable(_tableModel);

		_table.setFillsViewportHeight(true);
		_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(_table);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public void setStore(ObservationStore store) { _tableModel.setStore(store); }
	public ObservationStore getStore() { return _tableModel.getStore(); }
	public JTable getTable() { return _table; }
	public ObservationTableModel getTableModel() { return _tableModel; }
	
	public void addSelectionListener(ListSelectionListener l) {
		_table.getSelectionModel().addListSelectionListener(l);
	}

}
