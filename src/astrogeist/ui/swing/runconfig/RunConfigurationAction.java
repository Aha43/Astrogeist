package astrogeist.ui.swing.runconfig;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;

import aha.common.logging.Log;
import astrogeist.engine.abstraction.integration.runconfig.RunConfiguration;

public final class RunConfigurationAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final Logger logger = Log.get(this);
	
	private final RunConfiguration runConfiguration;
	
	public RunConfigurationAction(RunConfiguration runConfiguration) {
		super(runConfiguration.name());
		this.runConfiguration = runConfiguration;
	}

	@Override public final void actionPerformed(ActionEvent e) {
		logger.info("Running : " + this.runConfiguration.name());
	}

}
