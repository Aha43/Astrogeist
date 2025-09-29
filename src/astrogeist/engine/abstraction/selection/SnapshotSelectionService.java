package astrogeist.engine.abstraction.selection;

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
     *   Entry point for UI sources. Must be called on the EDT.
     * </p>
     * @param snapshot
     * @param source
     */
    void select(Map<String, TimelineValue> snapshot, Class<? extends SelectionSourceTag> source);
    
    /**
     * <p>
     *   Gets current selection.
     * </p>
     * @return the current snapshot or {@code null} if non selected.
     */
    Map<String, TimelineValue> getCurrent();
}
