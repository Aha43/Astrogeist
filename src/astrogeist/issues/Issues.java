package astrogeist.issues;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class Issues {
	private List<Issue> issues = new ArrayList<>();
	
	public void add(Path path, String message) { add(path, message, null, null); }
	public void add(Path path, String message, String solutionKey) { add(path, message, null, solutionKey); }
	public void add(Path path, String message, Exception x) { add(path, message, x, null); }
	public void add(Path path, String message, Exception x, String solutionKey) {
		this.issues.add(new Issue(path, message, x, solutionKey)); }
	public List<Issue> getIssues() { return this.issues; }
}
