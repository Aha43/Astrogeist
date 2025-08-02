package astrogeist.scanner;

import astrogeist.store.ObservationStore;

public interface Scanner {
	void scan(ObservationStore store) throws Exception;
	
	Scanner[] EmptyArray = new Scanner[0];
}
