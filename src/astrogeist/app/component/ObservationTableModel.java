package astrogeist.app.component;

import javax.swing.table.AbstractTableModel;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import astrogeist.store.*;

public final class ObservationTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private final List<Instant> timestamps = new ArrayList<>();
	private final List<String> columns = new ArrayList<>();
	private final Map<Instant, Map<String, String>> rows = new LinkedHashMap<>();
	private static final String TIME_COLUMN = "Time";

	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
		.withZone(ZoneId.systemDefault());

	public void setStore(ObservationStore store) {
		timestamps.clear();
		columns.clear();
		rows.clear();

		// Discover columns from all properties
		var dynamicColumns = new TreeSet<String>();
		for (Instant t : store.timestamps()) {
			var snapshot = store.snapshot(t);
			timestamps.add(t);
			rows.put(t, snapshot);
			dynamicColumns.addAll(snapshot.keySet());
		}

		// Always include "Time" as first column
		columns.add(TIME_COLUMN);
		columns.addAll(dynamicColumns);

		fireTableStructureChanged();
	}

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
