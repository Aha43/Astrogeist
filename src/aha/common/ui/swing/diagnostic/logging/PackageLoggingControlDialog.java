package aha.common.ui.swing.diagnostic.logging;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JDialog;

import astrogeist.ui.swing.component.logging.LoggingLevelsPanel;

public final class PackageLoggingControlDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private PackageLoggingControlDialog(Frame parent) {
		super(parent, "Logging by Package");
		super.setModal(true);
		
		var loggingLevelsPanel = new LoggingLevelsPanel();
		super.add(loggingLevelsPanel, BorderLayout.CENTER);
		
		super.pack();
	}
	
	private static Dialog _dialog = null;
	
	public static final void show(Frame frame) {
		if (_dialog == null) _dialog = new PackageLoggingControlDialog(frame);
		_dialog.setVisible(true);
	}

}
