package astrogeist.scanner;

import java.util.ArrayList;
import java.util.List;

import astrogeist.scanner.capdata.CapDataScanner;
import astrogeist.scanner.userdata.UserDataScanner;
import astrogeist.timeline.Timeline;

public final class CompositeScanner implements Scanner {
	private final List<Scanner> _scanners = new ArrayList<>();
	
	public CompositeScanner() {
		addScanners(CapDataScanner.createScanners());
		addScanners(new UserDataScanner());
	}

	@Override
	public void scan(Timeline timeline) throws Exception {
		for (var scanner : _scanners) scanner.scan(timeline); }
	
	public void addScanners(Scanner... scanners) { 
		for (var scanner : scanners) _scanners.add(scanner); }
}
