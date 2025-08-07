package astrogeist.ui.swing.dialog.files;

import java.awt.BorderLayout;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.files.FilesTablePanel;
import astrogeist.ui.swing.dialog.ModalDialogBase;

public final class FileListDialog extends ModalDialogBase {
	private static final long serialVersionUID = 1L;
	
	private final FilesTablePanel tablePanel = new FilesTablePanel(); 

	private FileListDialog(
		App app, 
		astrogeist.engine.typesystem.Type.DiskFile fileType, 
		Instant timestamp,
		List<Path> files) {
		
		super(app, fileType.getFileTypeName().toUpperCase() + " Files", true);
		add(tablePanel, BorderLayout.CENTER);
		tablePanel.setFiles(fileType, timestamp, files);
		pack();
	}
	
	public static void show(
		App app, 
		astrogeist.engine.typesystem.Type.DiskFile fileType,
		Instant timestamp,
		List<Path> files) {
		
		new FileListDialog(app, fileType, timestamp, files).setVisible(true); 
	}
}
