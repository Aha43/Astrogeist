package astrogeist.ui.swing.integration.runconfig;

import aha.common.taskrunner.TaskRunContext;
import aha.common.util.AttributeObject;
import astrogeist.engine.integration.runconfig.TaskStepBase;
import astrogeist.engine.timeline.Snapshot;

public final class VerifyFitsFileRunStep extends TaskStepBase {

	//public VerifyFitsFileRunStep(Snapshot snapshot) { }

	protected VerifyFitsFileRunStep(String label, int weight) {
		super(label, weight);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(TaskRunContext ctx, AttributeObject ctxtData) 
		throws Exception {
		// TODO Auto-generated method stub
		
	}

}
