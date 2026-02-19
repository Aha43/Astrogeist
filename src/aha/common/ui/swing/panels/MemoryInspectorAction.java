package aha.common.ui.swing.panels;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import static aha.common.ui.swing.panels.MemoryInspectorDialog.show;

/**
 * <p>
 *   {@link Action} to show the
 *   {@link MemoryInspectorDialog}.
 * </p>
 */
public final class MemoryInspectorAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final Frame frame;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param frame {@link Frame} that is parent of dialog to show, may be
	 *              {@code null}.
	 */
	public MemoryInspectorAction(Frame frame) {
		super("Inspect Memory...");
		this.frame = frame;
	}

	@Override public final void actionPerformed(ActionEvent e) { show(frame); }
}
