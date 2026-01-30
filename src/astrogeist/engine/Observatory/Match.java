package astrogeist.engine.observatory;

import static java.util.Objects.requireNonNull;

import java.util.List;

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
public final class Match {
	private final Configuration configuration;
	private final List<String> missing; // selected but not in config
	private final List<String> extra;   // in config but not selected

	private final int intersectionCount;
	private final int selectedCount;
	private final int configCount;

	public Match(
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

	public final int intersectionCount() { return intersectionCount; }

	/**
	 * <p>
	 *   Jaccard similarity on sets (0..1).
	 *   Useful to rank "closest configurations" when no exact match exists.
	 * </p>
	 */
	public final double jaccard() {
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
	public final double selectionCoverage() {
		if (selectedCount == 0) return 1.0;
		return ((double) intersectionCount) / selectedCount;
	}

	/**
	 * <p>
	 *   True if config instrument set equals selected instrument set.
	 * </p>
	 */
	public final boolean isExactSetMatch() {
		return missing.isEmpty() && extra.isEmpty(); }
}