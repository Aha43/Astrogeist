package astrogeist.ui.swing.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;

import astrogeist.Common;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.buttons.CloseDialogButton;

public abstract class DialogBase extends JDialog {
	private static final long serialVersionUID = 1L;
	
	protected final JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	protected final App app;
	
	protected DialogBase(App app, String title, boolean modal) { this(app, title, modal, false); }
	
	protected DialogBase(App app, String title, boolean modal, boolean addCloseButton) {
		super(Common.dialogsCentered() ? null : app.getFrame(), title, modal);
		if (Common.dialogsRelative()) super.setLocationRelativeTo(app.getFrame());
		super.setLayout(new BorderLayout());
		super.add(this.buttonPannel, BorderLayout.SOUTH);
		this.app = app;
		if (addCloseButton) this.buttonPannel.add(new CloseDialogButton(this));
	}
	
	protected final void showIt() {
		super.setAlwaysOnTop(true);
		super.toFront();
		super.setVisible(true);
		super.setAlwaysOnTop(false);
	}
	
}
