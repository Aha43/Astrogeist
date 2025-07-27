package astrogeist.app.component.observationstoreview;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;

import astrogeist.app.component.fileview.ObservationFilesPanel;
import astrogeist.app.component.propertiesview.PropertiesTablePanel;
import astrogeist.store.ObservationStore;

public final class ObservationStoreTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTable _table;
	private final ObservationStoreTableModel _tableModel;
	
	private final PropertiesTablePanel _propertiesTablePanel;
	private final ObservationFilesPanel _observationFilesPanel;

	public ObservationStoreTablePanel(
		PropertiesTablePanel propertiesTablePanel,
		ObservationFilesPanel observationFilesPanel) {
		
		super(new BorderLayout());
		
		_propertiesTablePanel = propertiesTablePanel;
		_observationFilesPanel = observationFilesPanel;
		
		_tableModel = new ObservationStoreTableModel();
		_table = new JTable(_tableModel);

		_table.setFillsViewportHeight(true);
		_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		var scrollPane = new JScrollPane(_table);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public void setStore(ObservationStore store) { 
		_tableModel.setStore(store); 
		_propertiesTablePanel.clear();
		_observationFilesPanel.clear();
	}
	
	public ObservationStore getStore() { return _tableModel.getStore(); }
	public JTable getTable() { return _table; }
	public ObservationStoreTableModel getTableModel() { return _tableModel; }
	
	public void addSelectionListener(ListSelectionListener l) {
		_table.getSelectionModel().addListSelectionListener(l);
	}

}
