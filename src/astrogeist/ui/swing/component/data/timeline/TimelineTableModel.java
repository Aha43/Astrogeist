package astrogeist.ui.swing.component.data.timeline;

import java.time.Instant;
import java.util.LinkedHashMap;

import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.abstraction.timeline.TimelineView;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.ui.swing.component.data.timeline.view.AbstractTimelineViewTableModel;

/**
 * <p>
 *   Table model showing the non filtered time line. 
 * </p>
 */
public final class TimelineTableModel extends AbstractTimelineViewTableModel {
	private static final long serialVersionUID = 1L;
	
	private Timeline timeline = null;
	
	public final void timeline(Timeline timeline) { 
		this.timeline = timeline;
		initialize();
	}
	
	@Override public final TimelineView getTimelineView() {
		return this.timeline(); }
	
	public final Timeline timeline() { return this.timeline; }
	
	public final void update(Instant time, 
		LinkedHashMap<String, TimelineValue> values) {
	    
		for (var e : values.entrySet()) {
	        var key = e.getKey();
	        var tlv = e.getValue();
	        if (tlv == TimelineValue.Empty) timeline.remove(time, key);
	        else                            timeline.upsert(time, key, tlv);
	    }

	    int rowIndex = super.timestamps.indexOf(time);
	    if (rowIndex >= 0) fireTableRowsUpdated(rowIndex, rowIndex);
	}
	
	public final void update(Instant time, String name, TimelineValue tlv) {
		if (tlv == TimelineValue.Empty) timeline.remove(time, name);
		else                            timeline.upsert(time, name, tlv);
		        
		int rowIndex = super.timestamps.indexOf(time);
		if (rowIndex >= 0) fireTableRowsUpdated(rowIndex, rowIndex);
	}
}
