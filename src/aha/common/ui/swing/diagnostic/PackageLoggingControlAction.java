package aha.common.ui.swing.diagnostic;

import static aha.common.ui.swing.diagnostic.PackageLoggingControlDialog.show;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public final class PackageLoggingControlAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final Frame parent;
	
	public PackageLoggingControlAction(Frame parent) { 
		super("Logging by Package..."); 
		this.parent = parent;
	}

	@Override public final void actionPerformed(ActionEvent e) {
		show(this.parent); }
}
