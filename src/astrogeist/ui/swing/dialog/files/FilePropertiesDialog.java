package astrogeist.ui.swing.dialog.files;

import java.awt.BorderLayout;

import astrogeist.engine.file.FileRecord;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.files.FilePropertiesPanel;
import astrogeist.ui.swing.dialog.ModalDialogBase;

public class FilePropertiesDialog extends ModalDialogBase {
	private static final long serialVersionUID = 1L;
	
	private FilePropertiesDialog(App app, FileRecord file) {
		super(app, "File Properties", true);
		var panel = new FilePropertiesPanel(file);
		super.add(panel, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(app.getFrame());
	}

	public static final void show(App app, FileRecord file) { 
		new FilePropertiesDialog(app, file).setVisible(true); }
}
