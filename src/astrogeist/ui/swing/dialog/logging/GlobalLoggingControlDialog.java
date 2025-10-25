package astrogeist.ui.swing.dialog.logging;

import java.awt.Dialog;

import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.logging.GlobalLoggingPanel;
import astrogeist.ui.swing.dialog.DialogBase;

public final class GlobalLoggingControlDialog extends DialogBase {
    private static final long serialVersionUID = 1L;

    private GlobalLoggingControlDialog(App app) {
        super(app, "Logging", false);
        setContentPane(new GlobalLoggingPanel(this::dispose));
        pack();
    }
    
    private static Dialog _dialog = null;
    
    public static void showDialog(App app) {
    	if (_dialog == null) _dialog = new GlobalLoggingControlDialog(app);
    	_dialog.setVisible(true);
    }
    
}
