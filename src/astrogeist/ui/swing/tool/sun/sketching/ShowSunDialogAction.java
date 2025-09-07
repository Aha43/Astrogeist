package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public final class ShowSunDialogAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	public ShowSunDialogAction() { super("Sun"); }

	@Override
	public void actionPerformed(ActionEvent e) {
		var dlg = new SunSketchingDialog();
		dlg.setVisible(true);
	}

}
