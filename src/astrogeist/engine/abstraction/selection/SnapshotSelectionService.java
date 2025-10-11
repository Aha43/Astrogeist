package astrogeist.engine.abstraction.selection;

import java.time.Instant;
import java.util.Map;

import astrogeist.engine.timeline.TimelineValue;

/**
 * <p>
 *   Interface for services communicating user's selection of snapshot.
 * </p>
 */
public interface SnapshotSelectionService {

	/**
     * <p>
     *   Adds an
     *   {@link Augmenter}.
     * </p>
     * @param a the {@link Augmenter} to add.
     */
    void addAugmenter(Augmenter a);
    
    /**
     * <p>
     *   Adds listener for snapshot selection events.
     * </p>
     * @param l the listener to add.
     */
    void addListener(SnapshotListener l);
    
    /**
     * <p>
     *   Removes listener for snapshot selection events.
     * </p>
     * @param l the listener to add.
     */
    void removeListener(SnapshotListener l);
    
    /**
     * <p>
     *   Entry point for UI sources when a selection made. Must be called on the EDT.
     * </p>
     * @param timestamp the time selected at. 
     * @param snapshot  the selected.
     */
    void selected(Instant timestamp, Map<String, TimelineValue> snapshot);
    
    /**
     * <p>
     *   Entry point for UI sources when selection cleared. Must be called on the EDT.
     * </p>
     */
    void cleared();
}
