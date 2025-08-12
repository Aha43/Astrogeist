package astrogeist.ui.swing.component.data.timeline;

import java.time.Instant;
import java.util.LinkedHashMap;

import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.ui.swing.component.data.timeline.view.AbstractTimelineViewTableModel;

public final class TimelineTableModel extends AbstractTimelineViewTableModel {
	private static final long serialVersionUID = 1L;
	
	public final void setTimeline(Timeline timeline) { super.setView(timeline); }
	
	public final Timeline getTimeline() { return (Timeline)this.view; }
	
	public final void update(Instant t, LinkedHashMap<String, TimelineValue> values) {
	    var timeline = (Timeline)super.view;
		
		// apply to Timeline first
	    for (var e : values.entrySet()) {
	        var key = e.getKey();
	        var tlv = e.getValue();
	        if (tlv == TimelineValue.Empty) {
	            timeline.remove(t, key);
	        } else {
	            timeline.upsert(t, key, tlv);
	        }
	    }

	    // now refresh the table’s row cache from Timeline
	    super.rows.put(t, new LinkedHashMap<>(super.view.snapshot(t)));

	    int rowIndex = super.timestamps.indexOf(t);
	    if (rowIndex >= 0) fireTableRowsUpdated(rowIndex, rowIndex);
	}
	
}
