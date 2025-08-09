package astrogeist.engine.abstraction;

import astrogeist.engine.issues.Issue;

public interface Solution {
	String getKey();
	String getSolutionDescription(Issue issue);
}
