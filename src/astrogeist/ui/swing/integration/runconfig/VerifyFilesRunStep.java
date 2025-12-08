package astrogeist.ui.swing.integration.runconfig;

import java.nio.file.Path;

import aha.common.exceptions.TaskStepException;
import aha.common.taskrunner.TaskRunContext;
import aha.common.taskrunner.TaskStepBase;
import aha.common.util.AttributeObject;
import astrogeist.engine.timeline.Snapshot;
import astrogeist.engine.typesystem.Type;

import static aha.common.util.Strings.quote;

public final class VerifyFilesRunStep extends TaskStepBase {
	private final Snapshot snapshot;
	private final Type.DiskFile fileType;
	
	public VerifyFilesRunStep(Type.DiskFile fileType, Snapshot snapshot) { 
		super("Verify files", 2);
		this.fileType = fileType;
		this.snapshot = snapshot;
	}

	@Override public void run(TaskRunContext ctx, AttributeObject ctxtData) 
		throws TaskStepException {
		
		var fileInfo = this.snapshot.getOfType(this.fileType);
		if (fileInfo == null) {
			throw new TaskStepException(this, "No files info found of type " + 
				quote(fileType));
		}
		
		var pathCollection = new PathCollection("fit");
		var n = fileInfo.size();
		for (var i = 0; i < n; i++) {
			var item = fileInfo.get(i);
			var ps = item.value();
			var path = Path.of(ps);
			pathCollection.add(path);
		}
	}

}
