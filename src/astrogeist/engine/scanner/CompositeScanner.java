package astrogeist.engine.scanner;

import java.util.ArrayList;
import java.util.List;

import astrogeist.engine.abstraction.Scanner;
import astrogeist.engine.abstraction.Timeline;
import astrogeist.engine.abstraction.UserDataIo;
import astrogeist.engine.scanner.capdata.CapDataScanner;
import astrogeist.engine.scanner.userdata.UserDataScanner;

public final class CompositeScanner implements Scanner {
	private final List<Scanner> _scanners = new ArrayList<>();
	
	public CompositeScanner(UserDataIo userDataIo) {
		addScanners(CapDataScanner.createScanners());
		addScanners(new UserDataScanner(userDataIo));
	}

	@Override
	public void scan(Timeline timeline) throws Exception {
		for (var scanner : _scanners) scanner.scan(timeline); }
	
	public void addScanners(Scanner... scanners) { 
		for (var scanner : scanners) _scanners.add(scanner); }
}
