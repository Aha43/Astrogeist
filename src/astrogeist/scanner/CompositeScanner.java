package astrogeist.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import astrogeist.scanner.sharpcap.SharpCapScanner;
import astrogeist.store.ObservationStore;

public final class CompositeScanner implements Scanner {
	
	private final List<Scanner> _scanners = new ArrayList<>();
	
	public CompositeScanner() {
		addScanner(new SharpCapScanner(new File("/Volumes/Extreme SSD/SharpCap")));
	}

	@Override
	public void scan(ObservationStore store) {
		for (var scanner : _scanners) scanner.scan(store);
	}
	
	public void addScanner(Scanner scanner) { _scanners.add(scanner); }

}
