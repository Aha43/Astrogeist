package aha.common.ui.swing.diagnostic;

import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JDialog;

import astrogeist.ui.swing.component.logging.GlobalLoggingPanel;

public final class GlobalLoggingControlDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private GlobalLoggingControlDialog(Frame parent) {
    	super(parent, "Logging");
    	super.setModal(true);
        
    	setContentPane(new GlobalLoggingPanel(this::dispose));
        
        pack();
    }
    
    private static Dialog _dialog = null;
    
    public final static void show(Frame parent) {
    	if (_dialog == null) _dialog = new GlobalLoggingControlDialog(parent);
    	_dialog.setVisible(true);
    }
    
}
