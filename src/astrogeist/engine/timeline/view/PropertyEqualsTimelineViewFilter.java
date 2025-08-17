package astrogeist.engine.timeline.view;

import java.time.Instant;

import astrogeist.engine.abstraction.TimelineView;

public final class PropertyEqualsTimelineViewFilter extends AbstractTimelineViewFilter {
	private final String key;
	private final String searched;
	
	public PropertyEqualsTimelineViewFilter(String key, String searched) {
		this.key = key;
		this.searched = searched;
	}
	
	public final String key() { return this.key; }
	public final String searched() { return this.searched; }
	
	@Override public final boolean accept(Instant time, TimelineView view) {
		var snapshot = view.snapshot(time);
		if (snapshot == null) return false;
		var tlv = snapshot.get(key);
        return tlv != null && searched.equals(tlv.value());
	}
	
	@Override public final String toString() { return key + " = " + searched; }
}
