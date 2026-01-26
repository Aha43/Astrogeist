package astrogeist.ui.swing.component.observatory;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import astrogeist.engine.observatory.Configuration;
import astrogeist.engine.observatory.Observatory;
import astrogeist.engine.observatory.ConfigurationMatcher;

/**
 * 3-panel selection UI:
 * - left: InstrumentPickerPanel
 * - middle: ConfigurationMatchTablePanel
 * - right: ConfigurationDetailsPanel
 *
 * Behavior:
 * - if no instruments selected: show all configurations (no "missing" filtering)
 * - if selected instruments exist: show "must include all" matches
 * - if none match: show closest suggestions
 */
public final class ConfigurationSelectionPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public interface SelectionListener {
		void configurationSelected(Configuration configuration);
	}

	private final Observatory observatory;
	private final ConfigurationMatcher matcher;

	private final InstrumentPickerPanel instrumentPicker = new InstrumentPickerPanel();
	private final ConfigurationMatchTablePanel matchTable = new ConfigurationMatchTablePanel();
	private final ConfigurationDetailsPanel detailsPanel;

	private SelectionListener selectionListener;

	public ConfigurationSelectionPanel(
		Observatory observatory,
		ConfigurationMatcher matcher,
		ConfigurationMatcher.InstrumentNamesProvider
			configInstrumentNamesProvider) {

		super(new BorderLayout(8, 8));
		this.observatory = requireNonNull(observatory, "observatory");
		this.matcher = requireNonNull(matcher, "matcher");
		this.detailsPanel = new ConfigurationDetailsPanel(
        requireNonNull(configInstrumentNamesProvider, 
        	"configInstrumentNamesProvider"));

		setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

		// Layout: left | (middle | right)
		var middleRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
			matchTable, detailsPanel);
		middleRight.setResizeWeight(0.55);

		var all = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, instrumentPicker,
			middleRight);
		all.setResizeWeight(0.28);

		add(all, BorderLayout.CENTER);

		wire();
		loadInitialData();
		refreshMatches();
	}

	public void setSelectionListener(SelectionListener l) {
		this.selectionListener = l; }

	public Configuration getSelectedConfiguration() {
		var m = matchTable.getSelectedMatch();
		return m != null ? m.configuration() : null;
	}

	// ---- wiring ----

	private void wire() {
		instrumentPicker.addSelectionListener(
			selectedNames -> refreshMatches());

		matchTable.addSelectionListener(match -> {
			detailsPanel.setMatch(match);
			if (selectionListener != null) {
				var c = match != null ? match.configuration() : null;
				selectionListener.configurationSelected(c);
			}
		});
	}

	private void loadInitialData() {
		// Assumes your Observatory can expose instrument names in registry order.
		// If you have observatory.instruments().names(), use that.
		instrumentPicker.setInstrumentNames(observatory.instrumentNames());
	}

	private void refreshMatches() {
		var selected = instrumentPicker.getSelectedInstrumentNames();
		var allConfigs = observatory.configurations();

		List<ConfigurationMatcher.Match> matches;
		String header;

		if (selected.isEmpty()) {
			// Special case: show all configs, ranked by name. We can model this as "closest" with empty selection,
			// but then jaccard=1 for empty configs; instead keep it simple:
			matches = allConfigs.stream()
				.map(c -> matcher.suggestClosest(List.of(c), List.of(), 1).get(0)) // a bit awkward
				.toList();

			// Better: compute matches directly without suggestions. We'll do it explicitly:
			matches = allConfigs.stream()
				.map(c -> directMatchForEmptySelection(c))
				.sorted((a, b) -> a.configuration().name().compareToIgnoreCase(b.configuration().name()))
				.toList();

			header = "No instruments selected — showing all configurations (" + matches.size() + ")";
			matchTable.setMatches(header, matches);
			return;
		}

		matches = matcher.findMustIncludeAll(allConfigs, selected);
		if (!matches.isEmpty()) {
			header = "Matching configurations (" + matches.size() + ")";
			matchTable.setMatches(header, matches);
			return;
		}

		// No matches: suggest closest
		matches = matcher.suggestClosest(allConfigs, selected, 10);
		header = "No direct matches — showing closest suggestions (" + matches.size() + ")";
		matchTable.setMatches(header, matches);
	}

	private ConfigurationMatcher.Match directMatchForEmptySelection(
		Configuration c) {
		
		// missing = empty, extra = all instruments in config
		// intersection = 0, selectedCount = 0, configCount = size
		List<String> configNames = (detailsPanel == null) ?
			List.of() : requireNonNullElseGet(
            // Use matcher’s provider if you prefer; this uses the details panel provider indirectly via a local lambda is not available.
            // We avoid that: rely on configuration exposing instrument names. Replace with your real API if needed.
            () -> c.instrumentNames()
            List::of);

		// For empty selection, "extra" is config names, "missing" empty.
		return new ConfigurationMatcher.Match(
				c,
				List.of(),
				configNames,
				0,
				0,
				configNames.size());
  }
}

