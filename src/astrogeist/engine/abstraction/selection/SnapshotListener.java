package astrogeist.engine.abstraction.selection;

import java.time.Instant;
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
	/**
	 * <p>
	 *   Invoked when snapshot selected.
	 * </p>
	 * @param timestamp the time of snapshot.
	 * @param snapshot  the selected snapshot.
	 */
	void onSnapshotSelected(Instant timestamp, Map<String, TimelineValue> snapshot);
	void onSelectionCleared();
}
