package astrogeist.engine.issues.solution;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import astrogeist.common.Guards;
import astrogeist.engine.abstraction.Solution;
import astrogeist.engine.issues.Issue;

public final class Solutions {
	private static Map<String, Solution> _solutions = new ConcurrentHashMap<>();
	
	static {
		var solution = new TimeExtractionFromPathFailedSolution(); 
		_solutions.put(solution.getKey(), solution);
	}
	
	public static Optional<String> getSolutionDescription(Issue issue) {
		var key = issue.solutionKey();
		if (key == null) return Optional.empty();
		var solution = _solutions.get(key);
		return solution == null ? Optional.empty() : Optional.of(solution.getSolutionDescription(issue)); 
	}
	
	private Solutions() { Guards.throwStaticClassInstantiateError(); }
}
