package astrogeist.engine.issues.solution;

import astrogeist.engine.issues.Issue;

public interface Solution {
	String getKey();
	String getSolutionDescription(Issue issue);
}
