package astrogeist.engine.abstraction.selection;

import java.util.Map;

import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.timeline.TimelineValue;

/**
 * <p>
 *   Listener for selections of 
 *   {@link Timeline} snapshots.
 * </p>
 */
public interface SnapshotListener {
	void onSnapshotSelected(
        Map<String, TimelineValue> snapshot, Class<? extends SelectionSourceTag> source); 
}
