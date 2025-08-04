package astrogeist.app.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

public class CloseDialogAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final JDialog dialog;
	
	public CloseDialogAction(JDialog dialog) { super("Close"); this.dialog = dialog; }

	@Override
	public void actionPerformed(ActionEvent e) { this.dialog.setVisible(false); }
}
