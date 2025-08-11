package astrogeist.ui.swing.component.data.timeline;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.resources.Time;
import astrogeist.engine.timeline.TimelineValue;

public final class TimelineTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private TimelineView view = null;   // read source (Timeline or FilteredTimelineView)
	private Timeline writer = null;     // optional: only if you want editing
	
	private final List<Instant> timestamps = new ArrayList<>();
	private final List<String> columns = new ArrayList<>();
	private final LinkedHashMap<Instant, Map<String, TimelineValue>> rows = new LinkedHashMap<>();
	
	private static final String TIME_COLUMN = "UTC";
	
	public final void setData(Timeline timeline) { setData(timeline, timeline); }

	public final void setData(TimelineView view, Timeline writer) {
	    this.view = view;
	    this.writer = writer;
	    reloadFromView();
	}
	
	private final void reloadFromView() {
		this.timestamps.clear();
	    this.columns.clear();
	    this.rows.clear();

	    this.columns.add(TIME_COLUMN);

	    for (Instant t : this.view.timestamps()) {
	        var snapshot = this.view.snapshot(t);
	        this.timestamps.add(t);
	        this.rows.put(t, snapshot);
	        // Optionally collect columns dynamically:
	        snapshot.keySet().forEach(k -> { if (!this.columns.contains(k)) this.columns.add(k); });
	    }
	    fireTableStructureChanged();	
	}
	
	public final void setColumnsToShow(List<String> columns) {
		this.columns.clear();
		this.columns.add(TIME_COLUMN);
		this.columns.addAll(columns);
		fireTableStructureChanged();
	}
	
	public final TimelineView getView() { return this.view; }
	public final Timeline getWriter() { return this.writer; } // may be null
	
	public final Map<String, TimelineValue> getSnapshotAt(int row) {
	    return this.view.snapshot(this.timestamps.get(row)); // read from view
	}
	
	public final void update(Instant t, LinkedHashMap<String, TimelineValue> values) {
	    if (writer == null) return; // or throw if editing required

	    // apply to underlying Timeline (source of truth)
	    for (var e : values.entrySet()) {
	        var key = e.getKey();
	        var tlv = e.getValue();
	        if (tlv == TimelineValue.Empty) writer.remove(t, key);
	        else writer.upsert(t, key, tlv);
	    }

	    // after mutation, the filter might include/exclude rows → reload
	    reloadFromView();
	}

	@Override public final int getRowCount() { return this.timestamps.size(); }
	@Override public final int getColumnCount() { return this.columns.size(); }
	@Override public final String getColumnName(int column) { return this.columns.get(column); }

	@Override public final Object getValueAt(int row, int col) {
	    var timestamp = this.timestamps.get(row);
	    if (col == 0) return Time.TimeFormatter.format(timestamp);

	    var columnKey = this.columns.get(col);
	    var r = this.rows.getOrDefault(timestamp, new LinkedHashMap<>());
	    var retVal = r.getOrDefault(columnKey, TimelineValue.Empty); // return TimelineValue
	    return retVal.value();
	}

	public final Instant getTimestampAt(int rowIndex) { return this.timestamps.get(rowIndex); }
	
	//@Override
	//public final Class<?> getColumnClass(int columnIndex) {
	//    return (columnIndex == 0) ? String.class : TimelineValue.class; // first col is time
	//}
	
}
