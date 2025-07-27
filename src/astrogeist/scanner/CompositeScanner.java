package astrogeist.scanner;

import java.util.ArrayList;
import java.util.List;

import astrogeist.scanner.sharpcap.SharpCapScanner;
import astrogeist.store.ObservationStore;

public final class CompositeScanner implements Scanner {
	
	private final List<Scanner> _scanners = new ArrayList<>();
	
	public CompositeScanner() {
		addScanners(SharpCapScanner.createScanners());
	}

	@Override
	public void scan(ObservationStore store) {
		for (var scanner : _scanners) scanner.scan(store);
	}
	
	public void addScanners(Scanner... scanners) { 
		for (var scanner : scanners) _scanners.add(scanner); 
	}

}
