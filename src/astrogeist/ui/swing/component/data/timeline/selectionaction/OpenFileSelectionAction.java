package astrogeist.ui.swing.component.data.timeline.selectionaction;

import astrogeist.engine.timeline.Snapshot;
import astrogeist.engine.typesystem.Type;
import astrogeist.ui.swing.util.UiFilesUtil;

public final class OpenFileSelectionAction extends AbstractSelectionAction {
	private final Type.DiskFile fileType;
	
	public OpenFileSelectionAction(Type.DiskFile fileType) {
		super("Open " + fileType.getFileTypeName() + " file");
		this.fileType = fileType;
	}

	@Override public final void Perform(Snapshot snapshot) {
		var path = getFile(snapshot);
		if (path == null) return;
		UiFilesUtil.openFile(path);
	}
	
	private final String getFile(Snapshot snapshot) {
		var values = snapshot.getOfType(this.fileType);
		if (values.size() == 0) return null;
		var first = values.get(0);
		return first.value();
	}
	
}
