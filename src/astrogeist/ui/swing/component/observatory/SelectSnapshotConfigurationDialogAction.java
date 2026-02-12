package astrogeist.ui.swing.component.observatory;

import static java.util.Objects.requireNonNull;

import java.awt.Frame;
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
import astrogeist.engine.userdata.UserDataIo;
import astrogeist.ui.swing.App;

public final class SelectSnapshotConfigurationDialogAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final Logger log = Log.get(this);

	// Injected
	private final App app;
	private final Frame frame;
	private final Observatory observatory;
	private final TimelineValuePool timelineValuePool;
	private final SnapshotSelectionService snapshotSelectionService;
	//private final TimelineManager timelineManager;
	private final UserDataIo userDataIo;
	// detcejnI
	
	private Tuple2<Instant, Snapshot> current = null;
	
	public SelectSnapshotConfigurationDialogAction(App app) {
		super("Configuration");
		this.app = requireNonNull(app, "app");

		this.frame = app.getFrame();
		this.observatory = app.service(Observatory.class);
		this.timelineValuePool = app.service(TimelineValuePool.class);
		this.snapshotSelectionService =
			app.service(SnapshotSelectionService.class);
		//this.timelineManager = app.service(TimelineManager.class);
		this.userDataIo = app.service(UserDataIo.class);

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

		var selected = SelectConfigurationDialog.showDialog(this.frame, 
			observatory, DefaultConfigurationMatcher.INSTANCE, code);
		
		if (selected != null) updateSnapshot(selected);
	}
	
	private final void updateSnapshot(Configuration selected) {
		try {
			var timelineManager = this.app.service(TimelineManager.class);
			var t = this.current.first();
			var name = "Configuration";
			var tlv = this.timelineValuePool.get(name, selected.id());
			timelineManager.update(t, name, tlv);
			this.userDataIo.save(t, "Configuration", tlv);
		} catch (Exception x) {
			
		}
	}

}
