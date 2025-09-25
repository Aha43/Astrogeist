package astrogeist.ui.swing.component.data.timeline.view;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import astrogeist.engine.abstraction.timeline.TimelineView;
import astrogeist.engine.resources.Time;
import astrogeist.engine.timeline.TimelineValue;

/**
 * <p>
 *   Table model showing timeline. Used to show both complete time line and filtered time line.
 * </p>
 */
public abstract class AbstractTimelineViewTableModel extends AbstractTableModel { 
	private static final long serialVersionUID = 1L;
	
	protected final List<Instant> timestamps = new ArrayList<>();
	protected final List<String> columns = new ArrayList<>();
	
	private static final String TIME_COLUMN = "UTC";
	
	protected abstract TimelineView getTimelineView();

	protected final void initialize() {
		var view = this.getTimelineView();
		this.timestamps.clear();
	    for (Instant t : view.timestamps()) this.timestamps.add(t);
	    super.fireTableDataChanged();
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

		var view = this.getTimelineView();
		var snapshot = view.snapshot(timestamp);
		var tlv = snapshot.getOrDefault(column, TimelineValue.Empty);
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
