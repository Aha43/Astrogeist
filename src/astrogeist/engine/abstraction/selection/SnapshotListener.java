package astrogeist.engine.abstraction.selection;

import java.time.Instant;

import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.timeline.Snapshot;

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
	void onSnapshotSelected(Instant timestamp, Snapshot snapshot);
	
	/**
	 * <p>
	 *   Invoked when selection cleared.
	 * </p>
	 */
	void onSelectionCleared();
}
