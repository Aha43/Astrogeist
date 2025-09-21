package astrogeist.ui.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import astrogeist.ui.swing.App;

/**
 * <p>
 *   Base class for dialogs.
 * </p>
 * <p>
 *   Use is optional.
 * </p>
 */
public abstract class DialogBase extends JDialog {
    private static final long serialVersionUID = 1L;

    protected final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
    protected final JPanel contentPanel = new JPanel(new BorderLayout(8, 8));
    protected final App app;

    protected DialogBase(App app, String title, boolean modal) {
        this(app, title, modal, false); }

    protected DialogBase(App app, String title, boolean modal, boolean addCloseButton) {
        super(app.getFrame(), title, modal);
        this.app = app;

        // Layout
        var cp = getContentPane();
        cp.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        cp.add(contentPanel, BorderLayout.CENTER);
        cp.add(buttonPanel, BorderLayout.SOUTH);

        if (addCloseButton) addCloseButton(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);

        // Keyboard: ESC closes
        registerEscapeToClose();
    }
    
    /**
     * <p>
     *   Subclasses can call this to set their main UI.
     * </p>
     * @param c {@link Component} that is the main UI.
     */
    protected final void setContent(Component c) { contentPanel.add(c, BorderLayout.CENTER); }
    
    /**
     * <p>
     *   Optional convenience to add a button. 
     * </p>
     * <p>
     *   Button will not be default, use
     *   {@link #addDefaultButton(JButton)} to add default button.
     * </p>
     * @param button {@link JButton} to add. 
     */
    protected final void addButton(JButton button) { 
    	this.buttonPanel.add(button); }
    
    /**
     * <p>
     *   Optional convenience to add a button. 
     * </p>
     * <p>
     *   Button will not be default, use
     *   {@link #addDefaultButton(Action)} to add default button.
     * </p>
     * @param action {@link Action} that button should perform. 
     */
    protected final void addButton(Action action) { 
    	this.addButton(new JButton(action)); }
    
    /**
     * <p>
     *   Optional convenience to add a button. 
     * </p>
     * <p>
     *   Button will not be default, use
     *   {@link #addDefaultButton(JButton)} to add default button.
     * </p>
     * @param button {@link JButton} to add.
     */
    protected final void addDefaultButton(JButton button) {
    	this.buttonPanel.add(button);
        super.getRootPane().setDefaultButton(button);
    }
    
    /**
     * <p>
     *   Optional convenience to add a button. 
     * </p>
     * <p>
     *   Button will not be default, use
     *   {@link #addDefaultButton(JButton)} to add default button.
     * </p>
     * @param action {@link Action} that button should perform.
     */
    protected final void addDefaultButton(Action action) { 
    	this.addDefaultButton(new JButton(action)); }

    /**
     * <p>
     *   Optional convenience to add a close button.
     * </p>
     * @param isCancel If {@code true} button label is Cancel;
     *                 if {@code false} button label is Close.
     */
    protected final void addCloseButton(boolean isCancel) {
    	var title = isCancel ? "Cancel" : "Close";
        var close = new JButton(title);
        close.addActionListener(e -> super.dispose());
        addButton(close);
    }
    
    protected final void addOkButton(Action action) {
    	var button = new JButton(action.getValue(Action.NAME).toString());
    	button.addActionListener(e -> {
    		super.dispose();
    		action.actionPerformed(e);
    	});
    	this.addDefaultButton(button);
    }
    
    /**
     * <p>
     *   Shows the dialog, packing and centering relative to owner.
     * </p>
     */
    public final void showIt() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::showIt);
            return;
        }
        pack();
        setLocationRelativeTo(getOwner()); // centers on parent
        if (super.isModal()) setVisible(true); // modal => blocks until closed
        else {
    		super.setAlwaysOnTop(true);
    		super.toFront();
    		super.setVisible(true);
    		super.setAlwaysOnTop(false);
        }
    }

    private final void registerEscapeToClose() {
        var im = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        var am = getRootPane().getActionMap();
        String key = "close-on-escape";
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), key);
        am.put(key, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override public void actionPerformed(ActionEvent e) { dispose(); }
        });
    }
    
}
