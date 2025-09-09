package astrogeist.ui.swing.dialog.logging;

import java.awt.BorderLayout;
import java.awt.Dialog;

import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.logging.LoggingLevelsPanel;
import astrogeist.ui.swing.dialog.DialogBase;

public final class PackageLoggingDialog extends DialogBase {
	private static final long serialVersionUID = 1L;
	
	// App app, String title, boolean modal, boolean addCloseButton
	private PackageLoggingDialog(App app) {
		super(app, "Logging by Package", false, true);
		
		var loggingLevelsPanel = new LoggingLevelsPanel();
		super.add(loggingLevelsPanel, BorderLayout.CENTER);
		super.pack();
		super.setSize(500, 500);
	}
	
	private static Dialog _dialog = null;
	
	public static final void show(App app) {
		if (_dialog == null) _dialog = new PackageLoggingDialog(app);
		_dialog.setVisible(true);
	}

}
