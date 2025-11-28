package astrogeist.engine.abstraction.selection;

import astrogeist.engine.timeline.Snapshot;

/**
 * <p>
 *   {@code Augmenter} s run first (before
 *   {@link SnapshotListener}s; may mutate/extend the snapshot synchronously. 
 * </p>
 */
public interface Augmenter {
	/**
	 * <p>
	 *   Give this
	 *   {@code Augmenter} a chance to augment the snapshot. 
	 * </p>
	 * @param snapshot Snapshot that may be augmented.
	 * @return {@code true} if changed snapshot; 
	 *         {@code false} if snapshot not changed.
	 * @throws Exception If failed.
	 */
	boolean augment(Snapshot snapshot) throws Exception;
}
