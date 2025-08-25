package astrogeist.engine.timeline.view.filters;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.logging.Log;
import astrogeist.engine.timeline.view.AbstractTimelineViewFilter;

public final class PropertiesTimelineViewFilter extends AbstractTimelineViewFilter {
	private final Logger logger = Log.get(this);
	
	private final LinkedHashMap<String, String> searched;
	
	public PropertiesTimelineViewFilter(Map<String, String> searched) {
		this.searched = new LinkedHashMap<>(searched); }

	@Override
	public boolean accept(Instant time, TimelineView view) {
		var snapshot = view.snapshot(time);
		if (snapshot == null) return false;
		
		logger.info(super.name() + "filters at time: " + time.toString());
		
		for (var e : this.searched.entrySet()) {
			var key = e.getKey();
			var value = e.getValue();
			
			var tlv = snapshot.get(key);
			if (tlv == null) return false;
			
			if (!value.equals(tlv.value())) return false;
		}
		
		return true;
	}
	
	@Override public String toString() {
		var sb = new StringBuilder();
		
		var first = true;
		for (var e : this.searched.entrySet()) {
			if (!first) sb.append(", ");
			sb.append(e.getKey()).append('=').append(e.getValue());
			first = false;
		}
		
		return sb.toString();
	}

}
