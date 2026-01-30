package astrogeist.ui.swing.component.observatory;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import astrogeist.engine.observatory.Configuration;
import astrogeist.engine.observatory.ConfigurationMatcher;
import astrogeist.engine.observatory.Observatory;

/**
 * <p>
 *   Modal dialog for selecting a configuration using the 3-panel selection UI.
 * </p>
 */
public final class SelectConfigurationDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private final ConfigurationSelectionPanel selectionPanel;

	private final JButton okButton = new JButton("OK");
	private final JButton cancelButton = new JButton("Cancel");

	private Configuration selectedConfiguration;

	SelectConfigurationDialog(Frame owner, Observatory observatory,
		ConfigurationMatcher matcher) {

		super(owner, "Select Configuration", true);
		requireNonNull(observatory, "observatory");
		requireNonNull(matcher, "matcher");

		this.selectionPanel = new ConfigurationSelectionPanel(observatory,
			matcher);

		buildUi();
		wireUi();

		okButton.setEnabled(false);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(1100, 650);
		setLocationRelativeTo(owner);
	}

	/**
	 * <p> 
	 *   Shows the dialog and returns the selected configuration, or null if
	 *   cancelled.
	 * </p>
	 */
	static Configuration showDialog(Frame owner, Observatory observatory,
		ConfigurationMatcher matcher) {

		var d = new SelectConfigurationDialog(owner, observatory, matcher);
		d.setVisible(true);
		return d.selectedConfiguration;
	}

	// ---- UI ----

	private void buildUi() {
		setLayout(new BorderLayout(8, 8));
		((JPanel) getContentPane()).setBorder(
			BorderFactory.createEmptyBorder(8, 8, 8, 8));

		add(selectionPanel, BorderLayout.CENTER);

		var buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		buttons.add(cancelButton);
		buttons.add(okButton);

		add(buttons, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(okButton);
	}

	private void wireUi() {
		// Selection changes from the selection panel:
		selectionPanel.addSelectionListener(config -> {
			// A selection change may be programmatic (auto-select first row).
			// We do NOT enable OK until userConfirmedSelection becomes true.
			selectedConfiguration = config;
			updateOkEnabled();
		});

		okButton.addActionListener(e -> onOk());
		cancelButton.addActionListener(e -> onCancel());
	}

	private void updateOkEnabled() {
		boolean hasSelection = selectedConfiguration != null;
		okButton.setEnabled(hasSelection);
	}

	private void onOk() { if (!okButton.isEnabled()) return; dispose(); }

	private void onCancel() { selectedConfiguration = null; dispose(); }
	
}
