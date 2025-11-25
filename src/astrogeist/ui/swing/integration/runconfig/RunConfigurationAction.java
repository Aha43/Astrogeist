package astrogeist.ui.swing.integration.runconfig;

import java.awt.event.ActionEvent;
import java.time.Instant;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.AbstractAction;

import aha.common.logging.Log;
import astrogeist.engine.abstraction.integration.runconfig.RunConfiguration;
import astrogeist.engine.abstraction.selection.SnapshotListener;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.timeline.TimelineValue;

public final class RunConfigurationAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final Logger logger = Log.get(this);
	
	private final RunConfiguration runConfiguration;
	
	public RunConfigurationAction(RunConfiguration rc,
		SnapshotSelectionService sss) {
		
		super(rc.name());
		super.setEnabled(false);
		
		this.runConfiguration = rc;
		
		sss.addListener(new SnapshotListener() {
			@Override public void onSnapshotSelected(Instant t,
				Map<String, TimelineValue> sh) {
				setEnabled(true);
			}
			
			@Override public void onSelectionCleared() { setEnabled(true); }
		});
	}

	@Override public final void actionPerformed(ActionEvent e) {
		logger.info("Running : " + this.runConfiguration.name());
	}

}
