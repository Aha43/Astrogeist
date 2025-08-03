package astrogeist.issues.solution;

import astrogeist.issues.Issue;

public interface Solution {
	String getKey();
	String getSolutionDescription(Issue issue);
}
