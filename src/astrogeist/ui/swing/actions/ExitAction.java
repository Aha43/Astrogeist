package astrogeist.ui.swing.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class ExitAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	public static Action INSTANCE = new ExitAction();
	
	private ExitAction() { super("Exit"); }

	@Override
	public void actionPerformed(ActionEvent e) { System.exit(0); }
}
