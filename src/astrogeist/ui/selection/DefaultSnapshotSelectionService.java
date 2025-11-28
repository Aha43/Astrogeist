package astrogeist.ui.selection;

import static aha.common.ui.swing.AhaSwingUtil.ensureEdt;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import astrogeist.engine.abstraction.selection.Augmenter;
import astrogeist.engine.abstraction.selection.SnapshotListener;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.timeline.Snapshot;

public final class DefaultSnapshotSelectionService
	implements SnapshotSelectionService {
    
	private final List<Augmenter> augmenters = new ArrayList<>();
	private final List<SnapshotListener> ll = new CopyOnWriteArrayList<>();

    @Override public final void addAugmenter(Augmenter a) { 
    	augmenters.add(Objects.requireNonNull(a)); }
    
    @Override public final void addListener(SnapshotListener l) {
    	ll.add(Objects.requireNonNull(l)); }
    
    @Override public final void removeListener(SnapshotListener l) {
    	ll.remove(Objects.requireNonNull(l)); }
    
    @Override public final void selected(Instant timestamp, Snapshot snapshot) {
    	ensureEdt();
        
    	if (snapshot == null) return;

        try { 
        	for (Augmenter a : augmenters) a.augment(snapshot);
        } catch (Exception ex) {
            // log + decide policy; for now, propagate partially augmented 
        	// snapshot
            ex.printStackTrace();
        }

        // Now notify downstream listeners in EDT, after augmentation
        for (var l : ll) l.onSnapshotSelected(timestamp, snapshot);
    }
    
    @Override public final void cleared() {
    	ensureEdt();
    	for (var l : ll) l.onSelectionCleared();
	}
	
}
