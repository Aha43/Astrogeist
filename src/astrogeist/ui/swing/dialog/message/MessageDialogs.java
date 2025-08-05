package astrogeist.ui.swing.dialog.message;

import java.awt.Component;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import astrogeist.Common;
import astrogeist.engine.logging.Log;

public final class MessageDialogs {
	public static void showError(String message, Exception x) { showError(null, message, x); }

    public static void showWarning(String message) { showWarning(null, message); }

    public static void showInfo(String message) { showInfo(null, message); }
	
	public static void showError(Component parent, String message, Exception x) {
		if (parent == null) 
			Log.error(message, x);
		else
			Log.get(parent).log(Level.SEVERE, message, x);
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private MessageDialogs() { Common.throwStaticClassInstantiateError(); }
}
