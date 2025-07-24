package astrogeist.scanner;

import java.io.File;

import astrogeist.store.ObservationStore;

public interface Scanner {
	void scan(ObservationStore store, File dir);
}
