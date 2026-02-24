package astrogeist.ui.swing.component.observatory.use;

import static java.util.Objects.requireNonNull;

import java.awt.event.ActionEvent;
import java.time.Instant;
import java.util.logging.Logger;

import javax.swing.AbstractAction;

import aha.common.abstraction.IdNames;
import aha.common.logging.Log;
import aha.common.tuple.Tuple2;
import astrogeist.engine.abstraction.TimelineManager;
import astrogeist.engine.abstraction.selection.SnapshotListener;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.observatory.Observatory;
import astrogeist.engine.observatory.Selection;
import astrogeist.engine.timeline.Snapshot;
import astrogeist.engine.typesystem.Type;
import astrogeist.engine.userdata.UserDataIo;
import astrogeist.ui.swing.App;

public final class SelectSnapshotConfigurationDialogAction
	extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	
	private final Logger log = Log.get(this);

	// Injected
	private final App app;
	private final IdNames idNames;
	private final Observatory observatory;
	private final TimelineValuePool timelineValuePool;
	private final SnapshotSelectionService snapshotSelectionService;
	private final UserDataIo userDataIo;
	// detcejnI
	
	private Tuple2<Instant, Snapshot> current = null;
	
	public SelectSnapshotConfigurationDialogAction(App app) {
		super("Configuration");
		this.app = requireNonNull(app, "app");
		
		this.idNames = app.service(IdNames.class);

		this.observatory = app.service(Observatory.class);
		this.timelineValuePool = app.service(TimelineValuePool.class);
		this.snapshotSelectionService =
			app.service(SnapshotSelectionService.class);
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
			Log.error(this.log, "no snapshot at update time");
			return;
		}

		var snapshot = this.current.second();
		
		var selection = new Selection(this.idNames, snapshot, this.observatory);
		
		var dlg = selection.isEmpty() ?
			new AxisConfigurationSelectionDialog(app) :
			new AxisConfigurationSelectionDialog(app, selection);
		
		var selected = dlg.showDialog();
		
		if (!selected.isEmpty()) this.updateSnapshot(selected);
	}
	
	private final void updateSnapshot(Selection selected) {
		try {
			var timelineManager = this.app.service(TimelineManager.class);
			var t = this.current.first();
			
			//
			// For every Axis (id) there is selected a configuration for:
			//
			//  1 get a TimeLineValue object which Type is Configuration and
			//    value is the id of the Configuration (encoding the items of
			//    the Configuration).
			//  2 Add the TimeLineValue object to the timeline at the current
			//    (selected) time indexed (name) by the axis id.
			//  3 Persist the TimeLineValue at the current (selected) time 
			//    indexed (name) by the axis id.
			//
			
			for (var axisId : selected.getAxisIds()) {
				// 1:
				var conf = selected.getConfigurationById(axisId);
				var tlv = this.timelineValuePool.get(Type.Configuration(),
					conf.id());
				// 2:
				timelineManager.update(t, axisId, tlv);
				// 3:
				this.userDataIo.save(t, axisId, tlv);
			}
		} catch (Exception x) {
			Log.error(this.log, "Failed to set selection of configurations", x);
		}
	}
	
}
