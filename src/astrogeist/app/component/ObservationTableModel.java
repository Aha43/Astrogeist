package astrogeist.app.component;

import javax.swing.table.AbstractTableModel;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import astrogeist.store.*;

public final class ObservationTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private ObservationStore _store = null;
	
	private final List<Instant> timestamps = new ArrayList<>();
	private final List<String> columns = new ArrayList<>();
	private final Map<Instant, Map<String, String>> rows = new LinkedHashMap<>();
	private static final String TIME_COLUMN = "Time";

	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
		.withZone(ZoneId.systemDefault());

	private List<String> _columnsToShow = new ArrayList<String>();
	
	public ObservationTableModel() {
		_columnsToShow.add("subject");
	}
	
	public void setStore(ObservationStore store) {
	    _store = store;

	    timestamps.clear();
	    columns.clear();
	    rows.clear();

	    // Always include "Time" as the first column
	    columns.add(TIME_COLUMN);

	    // Add only columns listed in _columnsToShow (if any)
	    if (_columnsToShow != null && !_columnsToShow.isEmpty()) {
	        columns.addAll(_columnsToShow);
	    }

	    // Load rows
	    for (Instant t : store.timestamps()) {
	        var snapshot = store.snapshot(t);
	        timestamps.add(t);
	        rows.put(t, snapshot);
	    }

	    fireTableStructureChanged();
	}
	
	public ObservationStore getStore() { return _store; }

	@Override
	public int getRowCount() { return timestamps.size(); }
	@Override
	public int getColumnCount() { return columns.size(); }
	@Override
	public String getColumnName(int column) { return columns.get(column); }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		var timestamp = timestamps.get(rowIndex);
		var column = columns.get(columnIndex);

		if (TIME_COLUMN.equals(column)) {
			return timeFormatter.format(timestamp);
		}

		var data = rows.getOrDefault(timestamp, Collections.emptyMap());
		return data.getOrDefault(column, "");
	}

	public Instant getTimestampAt(int rowIndex) { return timestamps.get(rowIndex); }

}
