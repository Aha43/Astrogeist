package astrogeist.ui.swing.integration.runconfig;

import java.util.logging.Logger;

import javax.swing.JMenu;

import aha.common.abstraction.io.appdata.AppDataManager;
import aha.common.logging.Log;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.appdata.runconfig.RunConfigurations;

import static aha.common.logging.Log.error;

public final class RunConfigurationsMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	private final Logger logger = Log.get(this);
	
	public RunConfigurationsMenu(AppDataManager adm,
		SnapshotSelectionService sss) {
		
		super("Run");
		try {
			var rcs = adm.load(RunConfigurations.class);
			for (var rc : rcs.configurations()) {
				var a = new RunConfigurationAction(rc, sss);
				super.add(a);
			}
		} catch (Exception x) { error(this.logger, x); }
	}

}
