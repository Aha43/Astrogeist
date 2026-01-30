package astrogeist.engine.observatory;

import java.util.Collection;
import java.util.List;

public interface ConfigurationMatcher {
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
	List<Match> findMustIncludeAll(
		Collection<Configuration> configurations,
		Collection<String> selectedInstrumentNames);
	
	/**
	 * <p>
	 *   Finds configurations whose instrument set equals the selected instrument
	 *   set (exact set match).
	 *   Order is not considered for equality in this Phase 1 matcher.
	 * </p>
	 */
	List<Match> findExactSetMatches(
		Collection<Configuration> configurations,
		Collection<String> selectedInstrumentNames);
	
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
	List<Match> suggestClosest(
		Collection<Configuration> configurations,
		Collection<String> selectedInstrumentNames,
		int limit);

}