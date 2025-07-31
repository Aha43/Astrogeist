package astrogeist.app.dialog.message;

import java.awt.Component;

import javax.swing.JOptionPane;

import astrogeist.Common;

public final class MessageDialogs {
	public static void showError(String message, Exception x) { showError(null, x, message); }

    public static void showWarning(String message) { showWarning(null, message); }

    public static void showInfo(String message) { showInfo(null, message); }
	
	public static void showError(Component parent, Exception x, String message) {
		System.err.println(message + ":" + x.getLocalizedMessage());
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
