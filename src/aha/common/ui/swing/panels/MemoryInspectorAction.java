package aha.common.ui.swing.panels;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import static aha.common.ui.swing.panels.MemoryInspectorDialog.show;

public final class MemoryInspectorAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final Frame frame;
	
	public MemoryInspectorAction(Frame frame) {
		super("Inspect Memory...");
		this.frame = frame;
	}

	@Override public final void actionPerformed(ActionEvent e) { show(frame); }
}
