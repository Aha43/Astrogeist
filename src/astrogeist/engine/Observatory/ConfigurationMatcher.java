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
 * This matcher is intentionally "simple and explainable":
 * it can filter configurations and compute a diff-like explanation
 * (missing/extra) and a similarity score for "closest" suggestions.
 * </p>
 */
public final class ConfigurationMatcher {

  /**
   * <p>
   *   Match result containing both the configuration and the explanation of
   *   how it differs from the user's selected instrument set.
   * </p>
   * <p>
   *   Ordering of {@code missing} and {@code extra} follows the configuration's
   *   instrument order where possible (because order matters in your domain).
   * </p>
   */
  public static final class Match {
    private final Configuration configuration;
    private final List<String> missing; // selected but not in config
    private final List<String> extra;   // in config but not selected

    private final int intersectionCount;
    private final int selectedCount;
    private final int configCount;

    Match(
        Configuration configuration,
        List<String> missing,
        List<String> extra,
        int intersectionCount,
        int selectedCount,
        int configCount) {

      this.configuration = requireNonNull(configuration, "configuration");
      this.missing = List.copyOf(requireNonNull(missing, "missing"));
      this.extra = List.copyOf(requireNonNull(extra, "extra"));
      this.intersectionCount = intersectionCount;
      this.selectedCount = selectedCount;
      this.configCount = configCount;
    }

    public final Configuration configuration() { return configuration; }

    /** 
     * Instrument names selected by the user but missing from the configuration. 
     */
    public final List<String> missing() { return missing; }

    /** 
     * Instrument names present in the configuration but not selected by the
     * user.
     */
    public final List<String> extra() { return extra; }

    public int intersectionCount() { return intersectionCount; }

    /**
     * <p>
     *   Jaccard similarity on sets (0..1).
     *   Useful to rank "closest configurations" when no exact match exists.
     * </p>
     */
    public double jaccard() {
      int union = selectedCount + configCount - intersectionCount;
      if (union == 0) return 1.0; // both empty
      return ((double) intersectionCount) / union;
    }

    /**
     * <p>
     *   Coverage of selection (0..1): how much of the user's selection the 
     *   config contains.
     *   When user ticks a few instruments, this tends to feel good as a
     *   ranking.
     */
    public double selectionCoverage() {
    	if (selectedCount == 0) return 1.0;
    	return ((double) intersectionCount) / selectedCount;
    }

    /**
     * <p>
     *   True if config instrument set equals selected instrument set.
     * </p>
     */
    public boolean isExactSetMatch() {
    	return missing.isEmpty() && extra.isEmpty(); }
  }

  /**
   * <p>
   *   Extracts instrument names from a configuration, preserving the
   *   configuration order.
   * </p>
   * <p>
   *   Adapt this to your Configuration API. Recommended is:
   *   {@code configuration.instruments().names()} or similar.
   * </p>
   */
  public interface InstrumentNamesProvider {
	  List<String> getInstrumentNames(Configuration configuration); }

  private final InstrumentNamesProvider namesProvider;

  public ConfigurationMatcher(InstrumentNamesProvider namesProvider) {
	  this.namesProvider = requireNonNull(namesProvider, "namesProvider"); }

  /**
   * <p>
   *   Finds configurations that contain ALL selected instruments (set-based),
   *   then ranks them by:
   * </p>
   * <ol>
   *   <li>fewer extras</li>
   *   <li>higher Jaccard</li>
   *   <li>configuration name (stable tie-break)</li>
   * </ol>
   */
  public List<Match> findMustIncludeAll(
      Collection<Configuration> configurations,
      Collection<String> selectedInstrumentNames) {

	  requireNonNull(configurations, "configurations");
	  Set<String> selected = normalizeToSet(selectedInstrumentNames);

	  List<Match> matches = new ArrayList<>();
	  for (Configuration c : configurations) {
		  Match m = matchOne(c, selected);
		  if (m.missing().isEmpty()) {
			  matches.add(m);
		  }
	  }

	  matches.sort(Comparator
		.comparingInt((Match m) -> m.extra().size())
        .thenComparingDouble(Match::jaccard).reversed()
        .thenComparing(m -> m.configuration().name()));

	  return matches;
  }

  /**
   * <p>
   *   Finds configurations whose instrument set equals the selected instrument
   *   set (exact set match).
   *   Order is not considered for equality in this Phase 1 matcher.
   * </p>
   */
  public List<Match> findExactSetMatches(
      Collection<Configuration> configurations,
      Collection<String> selectedInstrumentNames) {

    requireNonNull(configurations, "configurations");
    Set<String> selected = normalizeToSet(selectedInstrumentNames);

    List<Match> matches = new ArrayList<>();
    for (Configuration c : configurations) {
      Match m = matchOne(c, selected);
      if (m.isExactSetMatch()) {
        matches.add(m);
      }
    }

    matches.sort(Comparator.comparing(m -> m.configuration().name()));
    return matches;
  }

  /**
   * <p>
   *   Suggests the closest configurations even if they do not contain all
   *   selected instruments.
   *   This is used when filtering yields 0 results.
   * </p>
   * <p>
   *   Ranked by:
   *   <ol>
   *     <li>higher Jaccard</li>
   *     <li>higher selection coverage</li>
   *     <li>fewer extras</li>
   *   </ol>
   * </p>
   * @param limit max number of suggestions to return
   */
  public List<Match> suggestClosest(
      Collection<Configuration> configurations,
      Collection<String> selectedInstrumentNames,
      int limit) {

    requireNonNull(configurations, "configurations");
    if (limit <= 0) throw new IllegalArgumentException("limit must be > 0");

    Set<String> selected = normalizeToSet(selectedInstrumentNames);

    List<Match> matches = new ArrayList<>();
    for (Configuration c : configurations) {
      matches.add(matchOne(c, selected));
    }

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
	  List<String> configNamesOrdered =
			  namesProvider.getInstrumentNames(configuration);
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
	  for (String s : a) if (b.contains(s)) count++;
	  return count;
  }

  private static Set<String> normalizeToSet(Collection<String> names) {
	  requireNonNull(names, "names");
	  // LinkedHashSet preserves caller order (useful if selection order matters
	  // later)
	  Set<String> set = new LinkedHashSet<>();
	  for (String n : names) {
		  if (n == null) throw new NullPointerException(
				  "selectedInstrumentNames contains null");
		  set.add(n.trim());
	  }
	  return set;
  }
  
}

