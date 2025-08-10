package astrogeist.ui.swing.component.data.timeline;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.resources.Time;
import astrogeist.engine.timeline.TimelineValue;

public final class TimelineTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private Timeline timeline = null;
	
	private final List<Instant> timestamps = new ArrayList<>();
	private final List<String> columns = new ArrayList<>();
	private final LinkedHashMap<Instant, Map<String, TimelineValue>> rows = new LinkedHashMap<>();
	
	private static final String TIME_COLUMN = "UTC";

	public final void setData(Timeline timeline) {
		this.timeline = timeline;

		this.timestamps.clear();
		this.columns.clear();
		this.rows.clear();

	    // Always include "Time" as the first column
		this.columns.add(TIME_COLUMN);

	    // Load rows
	    for (Instant t : this.timeline.timestamps()) {
	        var snapshot = this.timeline.snapshot(t);
	        this.timestamps.add(t);
	        this.rows.put(t, snapshot);
	    }
	    fireTableStructureChanged();
	}
	
	public final void setColumnsToShow(List<String> columns) {
		this.columns.clear();
		this.columns.add(TIME_COLUMN);
		this.columns.addAll(columns);
		fireTableStructureChanged();
	}
	
	public final Timeline getData() { return this.timeline; }
	
	public final void update(Instant t, LinkedHashMap<String, TimelineValue> values) {
	    // apply to Timeline first
	    for (var e : values.entrySet()) {
	        var key = e.getKey();
	        var tlv = e.getValue();
	        if (tlv == TimelineValue.Empty) {
	            this.timeline.remove(t, key);
	        } else {
	            this.timeline.upsert(t, key, tlv);
	        }
	    }

	    // now refresh the table’s row cache from Timeline
	    rows.put(t, new LinkedHashMap<>(this.timeline.snapshot(t)));

	    int rowIndex = timestamps.indexOf(t);
	    if (rowIndex >= 0) fireTableRowsUpdated(rowIndex, rowIndex);
	}



	@Override public final int getRowCount() { return this.timestamps.size(); }
	@Override public final int getColumnCount() { return this.columns.size(); }
	@Override public final String getColumnName(int column) { return this.columns.get(column); }

	@Override public final  Object getValueAt(int rowIndex, int columnIndex) {
		var timestamp = this.timestamps.get(rowIndex);
		var column = this.columns.get(columnIndex);

		if (TIME_COLUMN.equals(column)) return Time.TimeFormatter.format(timestamp); 

		var data = this.rows.getOrDefault(timestamp, new LinkedHashMap<String, TimelineValue>());
		var tlv = data.getOrDefault(column, TimelineValue.Empty);
		return tlv.value();
	}

	public final Instant getTimestampAt(int rowIndex) { return this.timestamps.get(rowIndex); }
}
