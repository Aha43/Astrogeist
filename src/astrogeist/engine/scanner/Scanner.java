package astrogeist.engine.scanner;

import astrogeist.engine.abstraction.Timeline;

public interface Scanner {
	void scan(Timeline timeline) throws Exception;
	
	Scanner[] EmptyArray = new Scanner[0];
}
