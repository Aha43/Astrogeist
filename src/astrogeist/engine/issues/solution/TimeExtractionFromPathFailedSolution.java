package astrogeist.engine.issues.solution;

import astrogeist.engine.issues.Issue;

public class TimeExtractionFromPathFailedSolution implements Solution {
	public static final String KEY = TimeExtractionFromPathFailedSolution.class.getName();
	
	@Override
	public String getKey() { return KEY; }

	@Override
	public String getSolutionDescription(Issue issue) {
		var sb = new StringBuilder();
		sb.append("To fix this problem try add timestamp xml sections that matches the path: ")
			.append(issue.path().toString());
		return sb.toString();
	}
}
