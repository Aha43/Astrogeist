package astrogeist.ui.swing.component.data.timeline;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import astrogeist.engine.timeline.Timeline;
import astrogeist.engine.timeline.TimelineValue;

public final class TimelineTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private Timeline timeline = null;
	
	private final List<Instant> timestamps = new ArrayList<>();
	private final List<String> columns = new ArrayList<>();
	private final LinkedHashMap<Instant, LinkedHashMap<String, TimelineValue>> rows = new LinkedHashMap<>();
	private static final String TIME_COLUMN = "Time";

	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
		.withZone(ZoneId.systemDefault());

	public void setData(Timeline data) {
		this.timeline = data;

		this.timestamps.clear();
		this.columns.clear();
		this.rows.clear();

	    // Always include "Time" as the first column
		this.columns.add(TIME_COLUMN);

	    // Load rows
	    for (Instant t : data.timestamps()) {
	        var snapshot = data.snapshot(t);
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
	
	public Timeline getData() { return this.timeline; }
	
	public void update(Instant t, LinkedHashMap<String, TimelineValue> values) {
		var existing = rows.get(t);
		if (existing == null) return;

		for (var entry : values.entrySet()) {
			String key = entry.getKey();
			TimelineValue tlv = entry.getValue();
			var value = tlv.value();
			
			if (value == null || value.isEmpty() || value.equals("-")) {
				existing.remove(key); // delete
			} else {
				existing.put(key, tlv); // add/update
			}
		}

		int rowIndex = timestamps.indexOf(t);
		if (rowIndex >= 0) {
			fireTableRowsUpdated(rowIndex, rowIndex);
		}
	}

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

		var data = this.rows.getOrDefault(timestamp, new LinkedHashMap<String, TimelineValue>());
		var tlv = data.getOrDefault(column, TimelineValue.Empty);
		return tlv.value();
	}

	public Instant getTimestampAt(int rowIndex) { return this.timestamps.get(rowIndex); }
}
