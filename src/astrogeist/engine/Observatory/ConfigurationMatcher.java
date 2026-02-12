package astrogeist.engine.observatory;

import java.util.Collection;
import java.util.List;

public interface ConfigurationMatcher {
	/**
	 * <p>
	 *   Finds configurations that contain ALL selected items (set-based),
	 *   then ranks them by:
	 * </p>
	 * <ol>
	 *   <li>fewer extras</li>
	 *   <li>higher Jaccard</li>
	 *   <li>configuration name (stable tie-break)</li>
	 * </ol>
	 */
	List<Match> findMustIncludeAll(
		Collection<Configuration> configurations,
		Collection<String> selectedItemNames);
	
	/**
	 * <p>
	 *   Finds configurations whose items set equals the selected item set
	 *   (exact set match).
	 *   Order is not considered for equality in this Phase 1 matcher.
	 * </p>
	 */
	List<Match> findExactSetMatches(
		Collection<Configuration> configurations,
		Collection<String> selectedItemNames);
	
	/**
	 * <p>
	 *   Suggests the closest configurations even if they do not contain all
	 *   selected items.
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
	List<Match> suggestClosest(
		Collection<Configuration> configurations,
		Collection<String> selectedItemNames,
		int limit);
}
