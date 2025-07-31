package astrogeist.app.component.observationstoreview;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import astrogeist.store.ObservationStore;

public final class ObservationStoreTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private ObservationStore store = null;
	
	private final List<Instant> timestamps = new ArrayList<>();
	private final List<String> columns = new ArrayList<>();
	private final Map<Instant, Map<String, String>> rows = new LinkedHashMap<>();
	private static final String TIME_COLUMN = "Time";

	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
		.withZone(ZoneId.systemDefault());

	public void setStore(ObservationStore store) {
		this.store = store;

		this.timestamps.clear();
		this.columns.clear();
		this.rows.clear();

	    // Always include "Time" as the first column
		this.columns.add(TIME_COLUMN);

	    // Load rows
	    for (Instant t : store.timestamps()) {
	        var snapshot = store.snapshot(t);
	        this.timestamps.add(t);
	        this.rows.put(t, snapshot);
	    }
	    fireTableStructureChanged();
	}
	
	public void setColumnsToShow(List<String> columns) {
		this.columns.clear();
		this.columns.add(TIME_COLUMN);
		this.columns.addAll(columns);
		fireTableStructureChanged();
	}
	
	public ObservationStore getStore() { return this.store; }

	@Override
	public int getRowCount() { return this.timestamps.size(); }
	@Override
	public int getColumnCount() { return this.columns.size(); }
	@Override
	public String getColumnName(int column) { return this.columns.get(column); }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		var timestamp = this.timestamps.get(rowIndex);
		var column = this.columns.get(columnIndex);

		if (TIME_COLUMN.equals(column)) {
			return timeFormatter.format(timestamp);
		}

		var data = this.rows.getOrDefault(timestamp, Collections.emptyMap());
		return data.getOrDefault(column, "");
	}

	public Instant getTimestampAt(int rowIndex) { return this.timestamps.get(rowIndex); }
}
