package aha.common.ui.swing.diagnostic;

import static aha.common.ui.swing.diagnostic.GlobalLoggingControlDialog.show;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public final class GlobalLoggingControlAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final Frame parent;
	
	public GlobalLoggingControlAction(Frame parent) { 
		super("Global logging...");
		this.parent = parent;
	}

	@Override public final void actionPerformed(ActionEvent e) { 
		show(this.parent); }
}
