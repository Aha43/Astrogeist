package astrogeist.scanner;

import astrogeist.store.ObservationStore;

public interface Scanner {
	void scan(ObservationStore store);
	
	Scanner[] EmptyArray = new Scanner[0];
}
