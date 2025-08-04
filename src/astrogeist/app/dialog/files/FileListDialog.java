package astrogeist.app.dialog.files;

import java.awt.BorderLayout;
import java.nio.file.Path;
import java.util.List;

import astrogeist.app.App;
import astrogeist.app.component.data.fileview.FilesTablePanel;
import astrogeist.app.dialog.ModalDialogBase;

public final class FileListDialog extends ModalDialogBase {
	private static final long serialVersionUID = 1L;
	
	private final FilesTablePanel tablePanel = new FilesTablePanel(); 

	private FileListDialog(App app, String title, List<Path> files) {
		super(app, title, true);
		add(tablePanel, BorderLayout.CENTER);
		tablePanel.setFiles(files);
		pack();
	}
	
	public static void show(App app, String extension, List<Path> files) {
		new FileListDialog(app, extension.toUpperCase()+" Files", files).setVisible(true); }
}
