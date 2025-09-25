package astrogeist.ui.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.SwingUtilities;

import astrogeist.engine.abstraction.selection.Augmenter;
import astrogeist.engine.abstraction.selection.SelectionSourceTag;
import astrogeist.engine.abstraction.selection.SnapshotListener;
import astrogeist.engine.timeline.TimelineValue;

/** Single source of truth for "user accessed a snapshot". */
public final class SnapshotSelectionService {
    private final List<Augmenter> augmenters = new ArrayList<>();
    private final List<SnapshotListener> listeners = new CopyOnWriteArrayList<>();
    private volatile Map<String, TimelineValue> current;

    /**
     * <p>
     *   Adds an
     *   {@link Augmenter}.
     * </p>
     * @param a the {@link Augmenter} to add.
     */
    public final void addAugmenter(Augmenter a) { augmenters.add(Objects.requireNonNull(a)); }

    /**
     * <p>
     *   Adds 
     * </p>
     * @param l
     */
    public final void addListener(SnapshotListener l) { listeners.add(Objects.requireNonNull(l)); }
    
    public final void removeListener(SnapshotListener l) { listeners.remove(l); }

    /** Entry point for UI sources. Must be called on the EDT. */
    public final void select(
    	Map<String, TimelineValue> snapshot, Class<? extends SelectionSourceTag> source) {
        
    	ensureEdt();
        
    	if (snapshot == null) return;

        try {
            for (Augmenter a : augmenters) {
            	var changed = a.augment(snapshot);
            }
        } catch (Exception ex) {
            // log + decide policy; for now, propagate partially augmented snapshot
            ex.printStackTrace();
        }

        // Now notify downstream listeners in EDT, after augmentation
        for (var l : listeners) l.onSnapshotSelected(snapshot, source);
    }

    public final Map<String, TimelineValue> getCurrent() { return current; }

    private static final void ensureEdt() {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("select() must be called on EDT");
        }
    }
    
}
