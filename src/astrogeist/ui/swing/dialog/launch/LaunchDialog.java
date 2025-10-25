package astrogeist.ui.swing.dialog.launch;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import astrogeist.common.ImageUtil;
import astrogeist.engine.resources.Resources;
import astrogeist.ui.swing.component.logging.GlobalLoggingPanel;

public final class LaunchDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
	private final JCheckBox devMode = new JCheckBox("Run in development mode");
    private final JButton btnContinue = new JButton("Continue");
    private final JButton btnCancel = new JButton("Cancel");

    private boolean proceed = false;

    private LaunchDialog() {
        super(null, "Astrogeist — Startup", ModalityType.APPLICATION_MODAL);
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                proceed = false;
                dispose();
            }
        });
        
        var tabs = new JTabbedPane();
        this.addMainTab(tabs);
        this.addGlobalLoggingTab(tabs);
        
        setContentPane(tabs);
        pack();
    }
    
    private final void addMainTab(JTabbedPane pane) {
    	// --- Logo ---
    	var logo = ImageUtil.loadImage(Resources.LOGO_PATH);
        JLabel logoLabel = new JLabel();
        int logoSize = ImageUtil.dpiScaled(96); // looks good on dialogs
        if (logo != null) {
            var scaled = ImageUtil.scale(logo, logoSize, logoSize);
            logoLabel.setIcon(new ImageIcon(scaled));
        } else {
            logoLabel.setText("ASTROGEIST");
            logoLabel.setFont(logoLabel.getFont().deriveFont(Font.BOLD, 18f));
        }
        logoLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // --- Options panel (future-proof: just add more rows here later) ---
        JPanel options = new JPanel(new GridBagLayout());
        options.setBorder(BorderFactory.createTitledBorder("Startup Options"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_START;
        devMode.setSelected(true);
        options.add(devMode, c);

        // --- Buttons ---
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(btnCancel);
        buttons.add(btnContinue);

        btnContinue.addActionListener(e -> { proceed = true; dispose(); });
        btnCancel.addActionListener(e -> { proceed = false; dispose(); });

        // Enter = Continue, Esc = Cancel
        getRootPane().setDefaultButton(btnContinue);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
        getRootPane().getActionMap().put("cancel", new AbstractAction() {
            private static final long serialVersionUID = 1L;

			@Override public void actionPerformed(java.awt.event.ActionEvent e) {
                proceed = false; dispose(); }
        });

        // --- Layout ---
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.add(logoLabel, BorderLayout.WEST);
        content.add(options, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        
        pane.add("Astrogeist", content);
    }
    
    private void addGlobalLoggingTab(JTabbedPane pane) {
    	pane.add("Logging", new GlobalLoggingPanel());
    }

    /** Shows the dialog and returns the user choices. */
    public static LaunchOptions showStartupDialog() {
        LaunchDialog dlg = new LaunchDialog();
        dlg.setVisible(true); // blocks
        return new LaunchOptions(dlg.proceed, dlg.devMode.isSelected());
    }
}
