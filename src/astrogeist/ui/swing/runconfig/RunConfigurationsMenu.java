package astrogeist.ui.swing.runconfig;

import java.util.logging.Logger;

import javax.swing.JMenu;

import aha.common.abstraction.io.appdata.AppDataManager;
import aha.common.logging.Log;
import astrogeist.engine.appdata.runconfig.RunConfigurations;

import static aha.common.logging.Log.error;

public final class RunConfigurationsMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	private final Logger logger = Log.get(this);
	
	public RunConfigurationsMenu(AppDataManager appDataManager) {
		super("Run");
		try {
			var rcs = appDataManager.load(RunConfigurations.class);
			for (var rc : rcs.configurations()) {
				var a = new RunConfigurationAction(rc);
				super.add(a);
			}
		} catch (Exception x) { error(this.logger, x); }
	}

}
