package astrogeist.ui.swing.dialog.logging;

import java.awt.BorderLayout;
import java.awt.Dialog;

import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.logging.LoggingLevelsPanel;
import astrogeist.ui.swing.dialog.DialogBase;

public final class PackageLoggingControlDialog extends DialogBase {
	private static final long serialVersionUID = 1L;
	
	private PackageLoggingControlDialog(App app) {
		super(app, "Logging by Package", false, true);
		
		var loggingLevelsPanel = new LoggingLevelsPanel();
		super.add(loggingLevelsPanel, BorderLayout.CENTER);
		super.pack();
	}
	
	private static Dialog _dialog = null;
	
	public static final void show(App app) {
		if (_dialog == null) _dialog = new PackageLoggingControlDialog(app);
		_dialog.setVisible(true);
	}

}
