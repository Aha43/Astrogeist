package astrogeist.engine.abstraction;

public interface Scanner {
	void scan(Timeline timeline) throws Exception;
	
	Scanner[] EmptyArray = new Scanner[0];
}
