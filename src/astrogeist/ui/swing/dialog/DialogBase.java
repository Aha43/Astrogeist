package astrogeist.ui.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;

import astrogeist.ui.swing.App;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public abstract class DialogBase extends JDialog {
    private static final long serialVersionUID = 1L;

    protected final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
    protected final JPanel contentPanel = new JPanel(new BorderLayout(8, 8));
    protected final App app;

    protected DialogBase(App app, String title, boolean modal) {
        this(app, title, modal, false);
    }

    protected DialogBase(App app, String title, boolean modal, boolean addCloseButton) {
        super(app.getFrame(), title, modal);
        this.app = app;

        // Layout
        var cp = getContentPane();
        cp.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        cp.add(contentPanel, BorderLayout.CENTER);
        cp.add(buttonPanel, BorderLayout.SOUTH);

        if (addCloseButton) addCloseButton();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);

        // Keyboard: ESC closes
        registerEscapeToClose();
    }

    /** Subclasses can call this to set their main UI. */
    protected final void setContent(Component c) {
        contentPanel.add(c, BorderLayout.CENTER);
    }

    /** Optional convenience to add a button and (optionally) set it default. */
    protected final void addButton(JButton button, boolean asDefault) {
        buttonPanel.add(button);
        if (asDefault) getRootPane().setDefaultButton(button);
    }

    protected final void addCloseButton() {
        var close = new JButton("Close");
        close.addActionListener(e -> dispose());
        addButton(close, false);
    }

    /** Show the dialog, packing and centering relative to owner. */
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

    private void registerEscapeToClose() {
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
