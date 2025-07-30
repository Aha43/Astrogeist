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
	
	private ObservationStore _store = null;
	
	private final List<Instant> _timestamps = new ArrayList<>();
	private final List<String> _columns = new ArrayList<>();
	private final Map<Instant, Map<String, String>> _rows = new LinkedHashMap<>();
	private static final String TIME_COLUMN = "Time";

	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
		.withZone(ZoneId.systemDefault());

	public void setStore(ObservationStore store) {
	    _store = store;

	    _timestamps.clear();
	    _columns.clear();
	    _rows.clear();

	    // Always include "Time" as the first column
	    _columns.add(TIME_COLUMN);

	    // Load rows
	    for (Instant t : store.timestamps()) {
	        var snapshot = store.snapshot(t);
	        _timestamps.add(t);
	        _rows.put(t, snapshot);
	    }
	    fireTableStructureChanged();
	}
	
	public void setColumnsToShow(List<String> columns) {
		_columns.clear();
		_columns.add(TIME_COLUMN);
		_columns.addAll(columns);
		fireTableStructureChanged();
	}
	
	public ObservationStore getStore() { return _store; }

	@Override
	public int getRowCount() { return _timestamps.size(); }
	@Override
	public int getColumnCount() { return _columns.size(); }
	@Override
	public String getColumnName(int column) { return _columns.get(column); }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		var timestamp = _timestamps.get(rowIndex);
		var column = _columns.get(columnIndex);

		if (TIME_COLUMN.equals(column)) {
			return timeFormatter.format(timestamp);
		}

		var data = _rows.getOrDefault(timestamp, Collections.emptyMap());
		return data.getOrDefault(column, "");
	}

	public Instant getTimestampAt(int rowIndex) { return _timestamps.get(rowIndex); }
}
