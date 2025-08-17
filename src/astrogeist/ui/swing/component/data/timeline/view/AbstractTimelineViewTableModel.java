package astrogeist.ui.swing.component.data.timeline.view;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.resources.Time;
import astrogeist.engine.timeline.TimelineValue;

public abstract class AbstractTimelineViewTableModel extends AbstractTableModel { 
	private static final long serialVersionUID = 1L;
	
	protected final List<Instant> timestamps = new ArrayList<>();
	protected final List<String> columns = new ArrayList<>();
	protected final LinkedHashMap<Instant, Map<String, TimelineValue>> rows = new LinkedHashMap<>();
	
	private static final String TIME_COLUMN = "UTC";
	
	protected abstract TimelineView getTimelineView();

	protected final void initialize(TimelineView view) {
		this.timestamps.clear();
		this.columns.clear();
		this.rows.clear();

	    // Always include "Time" as the first column
		this.columns.add(TIME_COLUMN);

	    // Load rows
	    for (Instant t : view.timestamps()) {
	        var snapshot = view.snapshot(t);
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
	
	@Override public final int getRowCount() { return this.timestamps.size(); }
	@Override public final int getColumnCount() { return this.columns.size(); }
	@Override public final String getColumnName(int column) { return this.columns.get(column); }

	@Override public final  Object getValueAt(int row, int col) {
		var timestamp = this.timestamps.get(row);
		var column = this.columns.get(col);

		if (TIME_COLUMN.equals(column)) return Time.TimeFormatter.format(timestamp); 

		var data = this.rows.getOrDefault(timestamp, new LinkedHashMap<String, TimelineValue>());
		var tlv = data.getOrDefault(column, TimelineValue.Empty);
		return tlv.value();
	}

	public final Instant getTimestampAt(int row) { return this.timestamps.get(row); }
	
	public final Map<String, TimelineValue> getSnapshotAt(int row) {
		var time = this.timestamps.get(row);
		var view = this.getTimelineView();
		var retVal = view.snapshot(time);
		return retVal;
	}

}
