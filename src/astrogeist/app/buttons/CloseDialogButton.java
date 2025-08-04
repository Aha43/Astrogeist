package astrogeist.app.buttons;

import javax.swing.JButton;
import javax.swing.JDialog;

import astrogeist.app.actions.CloseDialogAction;

public final class CloseDialogButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	public CloseDialogButton(JDialog dialog) { super(new CloseDialogAction(dialog)); }
}
