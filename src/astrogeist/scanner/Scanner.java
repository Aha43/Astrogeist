package astrogeist.scanner;

import astrogeist.timeline.Timeline;

public interface Scanner {
	void scan(Timeline timeline) throws Exception;
	
	Scanner[] EmptyArray = new Scanner[0];
}
