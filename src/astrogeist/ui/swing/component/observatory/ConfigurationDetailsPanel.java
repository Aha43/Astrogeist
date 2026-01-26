package astrogeist.ui.swing.component.observatory;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import astrogeist.engine.observatory.ConfigurationMatcher;
import astrogeist.engine.observatory.ConfigurationMatcher.InstrumentNamesProvider;

// This is intentionally simple in V1 (no color highlighting yet).
/**
 * <p>
 *   Shows details for the selected configuration:
 *   - ordered instrument chain
 *   - missing/extra summary relative to current selection
 * </p>
 */
public final class ConfigurationDetailsPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final InstrumentNamesProvider namesProvider;

	private final JLabel titleLabel = new JLabel("No configuration selected");
	private final JLabel summaryLabel = new JLabel(" ");

	private final DefaultListModel<String> listModel = new DefaultListModel<>();
	private final JList<String> instrumentList = new JList<>(listModel);

	public ConfigurationDetailsPanel(InstrumentNamesProvider namesProvider) {
		super(new BorderLayout(6, 6));
		this.namesProvider = requireNonNull(namesProvider, "namesProvider");

		setBorder(BorderFactory.createTitledBorder("Configuration details"));

		var top = new JPanel(new BorderLayout(6, 6));
		top.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));
		top.add(titleLabel, BorderLayout.NORTH);
		top.add(summaryLabel, BorderLayout.SOUTH);

		add(top, BorderLayout.NORTH);
		add(new JScrollPane(instrumentList), BorderLayout.CENTER);

		clear();
	}

	public final void clear() {
		titleLabel.setText("No configuration selected");
		summaryLabel.setText(" ");
		listModel.clear();
	}

	public final void setMatch(ConfigurationMatcher.Match match) {
		if (match == null) { clear(); return; }

		var c = match.configuration();
		titleLabel.setText(Objects.requireNonNullElse(c.name(), "(unnamed)"));

		summaryLabel.setText("Missing: " + match.missing().size()
			+ "    Extra: " + match.extra().size()
			+ "    Jaccard: " + round3(match.jaccard()));

		listModel.clear();
		for (var n : namesProvider.getInstrumentNames(c))
			listModel.addElement(n);
	}

	private static String round3(double v) {
		double r = Math.round(v * 1000.0) / 1000.0;
		return Double.toString(r);
	}
}
