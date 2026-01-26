package astrogeist.ui.swing.component.observatory;

package astrogeist.gui.observatory;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import astrogeist.engine.observatory.Configuration;
import astrogeist.engine.observatory.Observatory;
import astrogeist.engine.observatory.ConfigurationMatcher;

/**
 * Modal dialog for selecting a configuration using the 3-panel selection UI.
 *
 * OK is enabled when either:
 *  - the user explicitly confirms a selection (click/double-click/Enter), or
 *  - there is exactly one candidate AND the user confirms (see note)
 *
 * Rationale: even if the UI auto-selects the first row, we usually want
 * an explicit user action to avoid accidental acceptance.
 */
public final class SelectConfigurationDialog extends JDialog {

  private static final long serialVersionUID = 1L;

  private final ConfigurationSelectionPanel selectionPanel;

  private final JButton okButton = new JButton("OK");
  private final JButton cancelButton = new JButton("Cancel");

  private Configuration selectedConfiguration;
  private boolean userConfirmedSelection = false;

  public SelectConfigurationDialog(
      Frame owner,
      Observatory observatory,
      ConfigurationMatcher matcher,
      ConfigurationMatcher.InstrumentNamesProvider configNamesProvider) {

    super(owner, "Select Configuration", true);
    requireNonNull(observatory, "observatory");
    requireNonNull(matcher, "matcher");
    requireNonNull(configNamesProvider, "configNamesProvider");

    this.selectionPanel = new ConfigurationSelectionPanel(
        observatory,
        matcher,
        configNamesProvider // note: your SelectionPanel ctor may differ; adapt accordingly
    );

    buildUi();
    wireUi();

    // Initial state
    okButton.setEnabled(false);

    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setSize(1100, 650);
    setLocationRelativeTo(owner);
  }

  /** Shows the dialog and returns the selected configuration, or null if cancelled. */
  public static Configuration showDialog(
      Frame owner,
      Observatory observatory,
      ConfigurationMatcher matcher,
      ConfigurationMatcher.InstrumentNamesProvider configNamesProvider) {

    SelectConfigurationDialog d =
        new SelectConfigurationDialog(owner, observatory, matcher, configNamesProvider);
    d.setVisible(true);
    return d.selectedConfiguration;
  }

  // ---- UI ----

  private void buildUi() {
    setLayout(new BorderLayout(8, 8));
    ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    add(selectionPanel, BorderLayout.CENTER);

    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
    buttons.add(cancelButton);
    buttons.add(okButton);

    add(buttons, BorderLayout.SOUTH);

    getRootPane().setDefaultButton(okButton);

    // ESC closes (cancel)
    getRootPane().registerKeyboardAction(
        e -> onCancel(),
        KeyStroke.getKeyStroke("ESCAPE"),
        JPanel.WHEN_IN_FOCUSED_WINDOW
    );
  }

  private void wireUi() {
    // Selection changes from the selection panel:
    selectionPanel.setSelectionListener(config -> {
      // A selection change may be programmatic (auto-select first row).
      // We do NOT enable OK until userConfirmedSelection becomes true.
      selectedConfiguration = config;
      updateOkEnabled();
    });

    okButton.addActionListener(e -> onOk());
    cancelButton.addActionListener(e -> onCancel());

    // User confirmation gestures:
    // We don’t have direct access to the JTable here unless you expose it.
    // Easiest: treat any mouse click anywhere in the selection panel that
    // results in a non-null selection as confirmation.
    //
    // This is robust and requires zero changes to your panels.
    selectionPanel.addMouseListener(new MouseAdapter() {
      @Override public void mousePressed(MouseEvent e) {
        // If user clicked inside the dialog and there is a selection, consider it confirmed.
        if (selectionPanel.getSelectedConfiguration() != null) {
          userConfirmedSelection = true;
          updateOkEnabled();
        }
      }
    });

    // Also treat double-click as “confirm and OK”
    selectionPanel.addMouseListener(new MouseAdapter() {
      @Override public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
          Configuration c = selectionPanel.getSelectedConfiguration();
          if (c != null) {
            userConfirmedSelection = true;
            selectedConfiguration = c;
            onOk();
          }
        }
      }
    });

    // Enter key should confirm if there is a selection
    getRootPane().registerKeyboardAction(
        e -> {
          Configuration c = selectionPanel.getSelectedConfiguration();
          if (c != null) {
            userConfirmedSelection = true;
            selectedConfiguration = c;
            onOk();
          }
        },
        KeyStroke.getKeyStroke("ENTER"),
        JPanel.WHEN_IN_FOCUSED_WINDOW
    );
  }

  private void updateOkEnabled() {
    boolean hasSelection = selectedConfiguration != null;
    okButton.setEnabled(hasSelection && userConfirmedSelection);
  }

  private void onOk() {
    if (!okButton.isEnabled()) return;
    dispose();
  }

  private void onCancel() {
    selectedConfiguration = null;
    dispose();
  }
}

