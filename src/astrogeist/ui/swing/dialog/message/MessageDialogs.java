package astrogeist.ui.swing.dialog.message;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import aha.common.Guards;
import aha.common.ImageUtil;
import astrogeist.engine.logging.Log;
import astrogeist.engine.resources.Resources;

/**
 * <p>
 *   Static methods to show astrogeist prompt, info, warning and error message dialogs.
 * </p>
 */
public final class MessageDialogs {
	private MessageDialogs() { Guards.throwStaticClassInstantiateError(); }
	
	private static final ImageIcon _errorIcon;
    private static final ImageIcon _warningIcon;
    private static final ImageIcon _infoIcon;
    private static final ImageIcon _questionIcon;
    
    static {
        BufferedImage logoGray = ImageUtil.loadImage(Resources.LOGO_PATH); 
        var iconSize = ImageUtil.dpiScaled(128);
        _errorIcon   = new ImageIcon(ImageUtil.scale(ImageUtil.tintPreserveLuminance(logoGray, new Color(220, 50, 47)), iconSize));
        _warningIcon = new ImageIcon(ImageUtil.scale(ImageUtil.tintPreserveLuminance(logoGray, new Color(255, 200, 0)), iconSize));
        _infoIcon    = new ImageIcon(ImageUtil.scale(ImageUtil.tintPreserveLuminance(logoGray, new Color(80, 180, 80)), iconSize));
        
        iconSize = ImageUtil.dpiScaled(64);
        _questionIcon = new ImageIcon(ImageUtil.scale(ImageUtil.tintPreserveLuminance(logoGray, new Color(0x3B,0x82,0xF6)), iconSize));
    }
    
    /**
     * <p>
     *   Show error message.
     * </p>
     * @param message Error message, may be null.
     * @param x       Exception.
     */
	public final static void showError(String message, Exception x) { showError(null, message, x); }
	
	/**
	 * <p>
	 *   Show warning message.
	 * </p>
	 * @param message Warning message.
	 */
    public final static void showWarning(String message) { showWarning(null, message); }
    
    /**
     * <p>
     *   Show information messages.
     * </p>
     * @param message Info message.
     */
    public final static void showInfo(String message) { showInfo(null, message); }
	
    /**
     * <p>
	 *   Show error message.  
	 * </p> 
     * @param parent  Dialog's parent component.
     * @param message Error message, may be null.
     * @param x       Exception.
     */
	public static void showError(Component parent, String message, Exception x) {
		message = message == null ? "" : message.trim();
		message = message.length() == 0 ? "Failure" : message;
		if (parent == null) Log.error(message, x);
		else                Log.get(parent).log(Level.SEVERE, message, x);
		if (x != null) {
			message += " : " + x.getClass().getSimpleName();
			var xmessage = x.getLocalizedMessage();
			if (xmessage != null && !xmessage.isBlank()) message += " : " + xmessage;
		}
		
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE, _errorIcon);
    }

	/**
	 * <p>
	 *   Show warning messages.
	 * </p>
	 * @param parent  Dialog's parent component.
	 * @param message Warning message.
	 */
    public final static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE, _warningIcon); }

    /**
     * <p>
	 *   Show information messages.
	 * </p> 
     * @param parent  Dialog's parent component.
     * @param message Info message.
     */
    public final static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE, _infoIcon); }
    
    /**
     * <p>
	 *   Prompt user.
	 * </p> 
     * @param parent  Dialog's parent component.
     * @param title   Dialog title.
     * @param message Prompt message.
     * @return Answer or null if canceled.
     */
    public final static String prompt(Component parent, String title, String message) {
    	return prompt(parent, title, message, ""); }
    
    /**
     * <p>
	 *   
	 * </p> 
     * @param parent  Dialog's parent component.
     * @param title   Dialog title.
     * @param message Prompt message.
     * @param initial Initial answer.
     * @return Answer or null if canceled.
     */
    public final static String prompt(Component parent, String title, String message, String initial) {
        return (String) JOptionPane.showInputDialog(
            parent, message, title,
            JOptionPane.QUESTION_MESSAGE, _questionIcon,
            null, initial
        );
    }
    
}
