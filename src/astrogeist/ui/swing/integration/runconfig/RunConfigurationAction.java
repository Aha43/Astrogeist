package astrogeist.ui.swing.integration.runconfig;

import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.time.Instant;
import java.util.logging.Logger;

import javax.swing.AbstractAction;

import aha.common.logging.Log;
import astrogeist.engine.abstraction.integration.runconfig.RunConfiguration;
import astrogeist.engine.abstraction.selection.SnapshotListener;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.timeline.Snapshot;
import astrogeist.engine.typesystem.Type;

public final class RunConfigurationAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final Logger logger = Log.get(this);
	
	private final RunConfiguration runConfiguration;
	
	private Snapshot selected = null;
	
	public RunConfigurationAction(RunConfiguration rc,
		SnapshotSelectionService sss) {
		
		super(rc.name());
		super.setEnabled(false);
		
		this.runConfiguration = rc;
		
		sss.addListener(new SnapshotListener() {
			@Override public void onSnapshotSelected(Instant t, 
				Snapshot snapshot) { setEnabled(true); selected = snapshot; }
			@Override public void onSelectionCleared() { 
				setEnabled(true); selected = null; }
		});
	}

	@Override public final void actionPerformed(ActionEvent e) {
		logger.info("Running : " + this.runConfiguration.name());
		if (selected == null) {
			logger.info("No snapshot selected");
			return;
		}
		
		var fitFileInfo = selected.getOfType(Type.FitFile());
		if (fitFileInfo == null) {
			logger.info("No fit file info found");
			return;
		}
		
		var pathCollection = new PathCollection("fit");
		var n = fitFileInfo.size();
		for (var i = 0; i < n; i++) {
			var item = fitFileInfo.get(i);
			var ps = item.value();
			var path = Path.of(ps);
			pathCollection.add(path);
		}
		
		
	}
}
