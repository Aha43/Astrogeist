package astrogeist.engine.observatory;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Matches configurations against a selected set of instrument names.
 *
 * <p>
 *   This matcher is intentionally "simple and explainable":
 *   it can filter configurations and compute a diff-like explanation
 *   (missing/extra) and a similarity score for "closest" suggestions.
 * </p>
 */
public final class DefaultConfigurationMatcher implements ConfigurationMatcher {
	private DefaultConfigurationMatcher() {}
	
	public final static DefaultConfigurationMatcher INSTANCE =
		new DefaultConfigurationMatcher();
	
	public final List<Match> findMustIncludeAll(
		Collection<Configuration> configurations,
		Collection<String> selectedInstrumentNames) {

		requireNonNull(configurations, "configurations");
		Set<String> selected = normalizeToSet(selectedInstrumentNames);

		List<Match> matches = new ArrayList<>();
		for (var c : configurations) {
			Match m = matchOne(c, selected);
			if (m.missing().isEmpty()) matches.add(m);
		}

		matches.sort(Comparator
			.comparingInt((Match m) -> m.extra().size())
			.thenComparingDouble(Match::jaccard).reversed()
			.thenComparing(m -> m.configuration().name()));

		return matches;
	}

	public final List<Match> findExactSetMatches(
		Collection<Configuration> configurations,
		Collection<String> selectedInstrumentNames) {

		requireNonNull(configurations, "configurations");
		Set<String> selected = normalizeToSet(selectedInstrumentNames);

		List<Match> matches = new ArrayList<>();
		for (Configuration c : configurations) {
			Match m = matchOne(c, selected);
			if (m.isExactSetMatch()) matches.add(m);
		}

		matches.sort(Comparator.comparing(m -> m.configuration().name()));
		return matches;
	}

	public final List<Match> suggestClosest(
		Collection<Configuration> configurations,
		Collection<String> selectedInstrumentNames,
		int limit) {

		requireNonNull(configurations, "configurations");
		if (limit <= 0) throw new IllegalArgumentException("limit must be > 0");

		Set<String> selected = normalizeToSet(selectedInstrumentNames);

		List<Match> matches = new ArrayList<>();
		for (var c : configurations) matches.add(matchOne(c, selected));

		matches.sort(Comparator
			.comparingDouble(Match::jaccard).reversed()
			.thenComparingDouble(Match::selectionCoverage).reversed()
			.thenComparingInt((Match m) -> m.extra().size())
			.thenComparing(m -> m.configuration().name()));

		if (matches.size() <= limit) return matches;
		return matches.subList(0, limit);
	}

	// ---- internals ----

	private Match matchOne(Configuration configuration, Set<String> selected) {
		List<String> configNamesOrdered = configuration.instrumentNames();
		Set<String> configSet = new LinkedHashSet<>(configNamesOrdered);

		// Missing: selected - config
		List<String> missing = new ArrayList<>();
		for (String s : selected)
			if (!configSet.contains(s)) missing.add(s);

		// Extra: config - selected (preserve config order)
		List<String> extra = new ArrayList<>();
		for (String n : configNamesOrdered)
			if (!selected.contains(n)) extra.add(n);

		int intersectionCount = intersectionCount(configSet, selected);
		return new Match(configuration, missing, extra, intersectionCount,
			selected.size(), configSet.size());
	}

	private static int intersectionCount(Set<String> a, Set<String> b) {
		int count = 0;
		for (var s : a) if (b.contains(s)) count++;
		return count;
	}

	private static Set<String> normalizeToSet(Collection<String> names) {
		requireNonNull(names, "names");
		
		// LinkedHashSet preserves caller order (useful if selection order
		// matters later)
		Set<String> set = new LinkedHashSet<>();
		for (var n : names) {
			if (n == null) throw new NullPointerException(
				"selectedInstrumentNames contains null");
			set.add(n.trim());
		}
		return set;
	}
  
}

