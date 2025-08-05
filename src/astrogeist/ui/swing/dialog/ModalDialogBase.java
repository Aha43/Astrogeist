package astrogeist.ui.swing.dialog;

import astrogeist.ui.swing.App;

public abstract class ModalDialogBase extends DialogBase {
	private static final long serialVersionUID = 1L;
	
	protected ModalDialogBase(App app, String title) { super(app, title, true); }
	
	protected ModalDialogBase(App app, String title, boolean addCloseButton) { 
		super(app, title, true, addCloseButton); }
}
