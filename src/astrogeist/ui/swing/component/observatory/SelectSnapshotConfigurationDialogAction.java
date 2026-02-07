package astrogeist.ui.swing.component.observatory;

import static java.util.Objects.requireNonNull;

import java.awt.event.ActionEvent;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;

import aha.common.logging.Log;
import aha.common.tuple.Tuple2;
import astrogeist.engine.abstraction.TimelineManager;
import astrogeist.engine.abstraction.selection.SnapshotListener;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.observatory.Configuration;
import astrogeist.engine.observatory.DefaultConfigurationMatcher;
import astrogeist.engine.observatory.Observatory;
import astrogeist.engine.timeline.Snapshot;
import astrogeist.ui.swing.App;

public final class SelectSnapshotConfigurationDialogAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final Logger log = Log.get(this);

	// Injected
	private final App app;
	private final TimelineValuePool timelineValuePool;
	private final SnapshotSelectionService snapshotSelectionService;
	// detcejnI
	
	private Tuple2<Instant, Snapshot> current = null;
	
	public SelectSnapshotConfigurationDialogAction(App app) {
		
		super("Configuration");
		this.app = requireNonNull(app, "app");

		this.timelineValuePool = app.service(TimelineValuePool.class);
		this.snapshotSelectionService =
			app.service(SnapshotSelectionService.class);

		super.setEnabled(false);
		this.setSnapshotListener();
	}
	
	private final void setSnapshotListener() {
		this.snapshotSelectionService.addListener(new SnapshotListener() {
			public void onSnapshotSelected(Instant t, Snapshot ss) {
				current = new Tuple2<>(t, ss);
				setEnabled(true);
			}

			public void onSelectionCleared() {
				current = null; setEnabled(false); }
		});
	}

	@Override public final void actionPerformed(ActionEvent e) {
		if (this.current == null) {
			this.log.log(Level.SEVERE, "no snapshot at update time");
			return;
		}

		var snapshot = this.current.second();
		var code = snapshot.valueAsString("Configuration");

		var frame = this.app.getFrame();
		var observatory = this.app.service(Observatory.class);
		var selected = SelectConfigurationDialog.showDialog(frame, observatory, 
			new DefaultConfigurationMatcher());
		
		if (selected != null) updateSnapshot(selected);
	}
	
	private final void updateSnapshot(Configuration selected) {
		var timelineManager = app.service(TimelineManager.class);

		var t = this.current.first();
		var name = "Configuration";
		var tlv = this.timelineValuePool.get(name, selected.id());
		timelineManager.update(t, name, tlv);
	}

}
