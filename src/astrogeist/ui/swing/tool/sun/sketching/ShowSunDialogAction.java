package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import astrogeist.ui.swing.App;

public final class ShowSunDialogAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final App app;
	
	public ShowSunDialogAction(App app) { super("Sun"); this.app = app; }

	@Override
	public void actionPerformed(ActionEvent e) {
		var dlg = new SunSketchingDialog(this.app);
		dlg.setVisible(true);
	}

}
