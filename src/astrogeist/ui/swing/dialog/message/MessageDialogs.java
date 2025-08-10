package astrogeist.ui.swing.dialog.message;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import astrogeist.Common;
import astrogeist.engine.logging.Log;
import astrogeist.engine.resources.Resources;
import astrogeist.engine.util.ImageUtil;

public final class MessageDialogs {
	private static final ImageIcon _errorIcon;
    private static final ImageIcon _warningIcon;
    private static final ImageIcon _infoIcon;
    
    static {
        BufferedImage logoGray = ImageUtil.loadImage(Resources.LOGO_PATH); 
        var iconSize = 256;
        _errorIcon   = new ImageIcon(ImageUtil.scale(ImageUtil.tintPreserveLuminance(logoGray, new Color(220, 50, 47)), iconSize));
        _warningIcon = new ImageIcon(ImageUtil.scale(ImageUtil.tintPreserveLuminance(logoGray, new Color(255, 200, 0)), iconSize));
        _infoIcon    = new ImageIcon(ImageUtil.scale(ImageUtil.tintPreserveLuminance(logoGray, new Color(80, 180, 80)), iconSize));
    }
    
	public static void showError(String message, Exception x) { showError(null, message, x); }

    public static void showWarning(String message) { showWarning(null, message); }

    public static void showInfo(String message) { showInfo(null, message); }
	
	public static void showError(Component parent, String message, Exception x) {
		if (parent == null) 
			Log.error(message, x);
		else
			Log.get(parent).log(Level.SEVERE, message, x);
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE, _errorIcon);
    }

    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE, _warningIcon); }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE, _infoIcon); }
    
    private MessageDialogs() { Common.throwStaticClassInstantiateError(); }
}
