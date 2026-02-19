package astrogeist.ui.swing.component.observatory.use;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import astrogeist.engine.observatory.Axis;
import astrogeist.engine.observatory.Configuration;
import astrogeist.engine.observatory.ConfigurationMatcher;
import astrogeist.engine.observatory.DefaultConfigurationMatcher;
import astrogeist.engine.observatory.Match;
import astrogeist.ui.swing.component.observatory.events.ConfigurationSelectionListener;

/**
 * <p>
 *   3-panel selection UI:
 *   - left: ItemPickerPanel
 *   - middle: ConfigurationMatchTablePanel
 *   - right: ConfigurationDetailsPanel
 * </p>
 * <p>
 *   Behavior:
 *   - if no items selected: show all configurations (no "missing" filtering)
 *   - if selected items exist: show "must include all" matches
 *   - if none match: show closest suggestions
 * </p>
 */
public final class ConfigurationSelectionPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final Axis axis;
	private final ConfigurationMatcher matcher;

	private final ItemPickerPanel itemPicker = new ItemPickerPanel();
	
	private final ConfigurationMatchTablePanel matchTable =
		new ConfigurationMatchTablePanel();
	private final ConfigurationDetailsPanel detailsPanel;

	private final List<ConfigurationSelectionListener> selectionListeners =
		new ArrayList<>();

	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * <p>
	 *   Uses the
	 *   {@link DefaultConfigurationMatcher}.
	 * </p>
	 * @param axis {@link Axis} which 
	 *             {@link Configuration}s are to be selected.
	 */
	public ConfigurationSelectionPanel(Axis axis) { this(axis, null); }
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param axis    {@link Axis} which 
	 *                {@link Configuration}s are to be selected.
	 * @param matcher {@link ConfigurationMatcher} implementation to use, if
	 *                {@code null} uses
	 *                {@link DefaultConfigurationMatcher}.
	 * @throws NullPointerException if either {@code axis}.
	 */
	public ConfigurationSelectionPanel(Axis axis, 
		ConfigurationMatcher matcher) {
		
		super(new BorderLayout(8, 8));
		
		this.matcher = matcher == null ? 
			DefaultConfigurationMatcher.INSTANCE : matcher;

		this.axis = requireNonNull(axis, "configuration");
		this.detailsPanel = new ConfigurationDetailsPanel();

		setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

		// Layout: left | (middle | right)
		var middleRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
			matchTable, detailsPanel);
		middleRight.setResizeWeight(0.55);

		var all = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, itemPicker,
			middleRight);
		all.setResizeWeight(0.28);

		add(all, BorderLayout.CENTER);

		wire();
		loadData();
		refreshMatches();
	}
	
	/**
	 * <p>
	 *   Gets the 
	 *   {@link Axis} this select 
	 *   {@link Configuration}s from.
	 * </p>
	 * @return {@link Axis}.
	 */
	public final Axis axis() { return this.axis; }
	
	/**
	 * <p>
	 *   Sets selected
	 *   {@link Configuration}.
	 * </p>
	 * @param id the id of
	 *           {@link Configuration} to set as selected.
	 * @throws NullPointerException if {@code id == null}.
	 * @throws IllegalArgumentException if {@code id} is the empty string.
	 */
	public final void setSelected(String id) {
		requireNonEmpty(id, "id");
		
		var conf = this.axis.getConfigurationById(id);
		if (conf == null) return;
		
		this.setSelected(conf);
	}
	
	/**
	 * <p>
	 *   Sets selected
	 *   {@link Configuration}.
	 * </p>
	 * @param conf the
	 *             {@link Configuration} to set as selected.
	 * @throws NullPointerException if {@code conf == null}.
	 */
	public final void setSelected(Configuration conf) {
		requireNonNull(conf, "conf");
		
		var names = conf.itemNames();
		if (names == null || names.isEmpty()) return;
		
		this.itemPicker.setSelectedItemNames(names);
	}
	
	/**
	 * <p>
	 *   Gets the selected (either programmatically or by user) 
	 *   {@link Configuration}.
	 * </p>
	 * @return {@link Configuration} or {@code null} if none selected.
	 */
	public final Configuration getSelectedConfiguration() {
		var m = matchTable.getSelectedMatch();
		return m != null ? m.configuration() : null;
	}

	/**
	 * <p>
	 *   Adds
	 *   {@link ConfigurationSelectionListener}.
	 * </p>
	 * @param l the listener to add.
	 * @throws NullPointerException if {@code l == null}.
	 */
	public final void addSelectionListener(ConfigurationSelectionListener l) {
		this.selectionListeners.add(requireNonNull(l, "l")); }
	
	/**
	 * <p>
	 *   Removes
	 *   {@link ConfigurationSelectionListener}.
	 * </p>
	 * @param l the listener to remove.
	 * @throws NullPointerException if {@code l == null}.
	 */
	public final void removeSelectionListener(
		ConfigurationSelectionListener l) {
		
		this.selectionListeners.remove(requireNonNull(l, "l")); }

	// ---- wiring ----

	private void wire() {
		itemPicker.addSelectionListener(
			selectedNames -> refreshMatches());

		matchTable.addSelectionListener(match -> {
			detailsPanel.setMatch(match);
			var c = match != null ? match.configuration() : null;
			for (var l : this.selectionListeners) l.configurationSelected(c);
		});
	}
	
	private void loadData() {
		this.itemPicker.setItemNames(
			this.axis.itemNames());
	}
	
	private void refreshMatches() {
		var selected = itemPicker.getSelectedItemNames();
		
		var allConfigs = this.axis.configurations().stream().toList();

		List<Match> matches;
		String header;

		if (selected.isEmpty()) {
			// Special case: show all configs, ranked by name. We can model this
			// as "closest" with empty selection, but then jaccard=1 for empty 
			// configs; instead keep it simple:
			matches = allConfigs.stream()
				.map(c -> // a bit awkward 
					matcher.suggestClosest(List.of(c), List.of(), 1).get(0))
				.toList();

			// Better: compute matches directly without suggestions.
			// We'll do it explicitly:
			matches = allConfigs.stream()
				.map(c -> directMatchForEmptySelection(c))
				.sorted((a, b) -> a.configuration().name()
					.compareToIgnoreCase(b.configuration().name())).toList();

			header = "No items selected — showing all configurations (" + 
				matches.size() + ")";
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
		header = "No direct matches — showing closest suggestions (" + 
				matches.size() + ")";
		matchTable.setMatches(header, matches);
	}

	private Match directMatchForEmptySelection(Configuration c) {
		
		// missing = empty, extra = all items in config
		// intersection = 0, selectedCount = 0, configCount = size
		List<String> configNames = c.itemNames();
		
		// For empty selection, "extra" is config names, "missing" empty.
		return new Match(
			c,
			List.of(),
			configNames,
			0,
			0,
			configNames.size());
	}
	
}
