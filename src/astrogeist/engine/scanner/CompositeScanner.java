package astrogeist.engine.scanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import astrogeist.engine.abstraction.Scanner;
import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.TimelineValuePool;
import astrogeist.engine.scanner.userdata.UserDataScanner;

public final class CompositeScanner implements Scanner {
	private final List<Scanner> _scanners = new ArrayList<>();
	
	public CompositeScanner(TimelineValuePool tvp) {
		var userDataScanner = new UserDataScanner(tvp); 
		addScanners(userDataScanner); 
	}

	@Override
	public void scan(Timeline timeline) throws Exception {
		for (var scanner : _scanners) scanner.scan(timeline); }
	
	public void addScanners(Scanner... scanners) { 
		for (var scanner : scanners) _scanners.add(scanner); }
	
	public void addScanners(Collection<Scanner> scanners) { 
		for (var scanner : scanners) _scanners.add(scanner); }
}
