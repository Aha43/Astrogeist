package astrogeist.scanner.sharpcap;

import java.io.File;
import java.time.LocalDate;

import astrogeist.scanner.Scanner;
import astrogeist.store.ObservationStore;

public final class SharpCapScanner implements Scanner {

	@Override
	public void scan(ObservationStore store, File dir) {
		scanRootDir(store, dir);// TODO Auto-generated method stub
	}
	
	private static void scanRootDir(ObservationStore store, File dir) {
		var files = dir.listFiles();
		if (files == null) return;
		
		for (var file : files) {
			if (file.isFile()) continue;
			
			var date = DateUtils.tryParseDate(file.getName());
			if (date == null) continue;
			
			scanSubjectsDir(store, file, date);
		}
	}
	
	private static void scanSubjectsDir(ObservationStore store, File dir, LocalDate date) {
		var files = dir.listFiles();
		if (files == null) return;
		
		for (var file : files) {
			if (file.isFile()) continue;
			
			var subject = file.getName();
			scanSubjectDir(store, file, date, subject);
		}
	}
	
	private static void scanSubjectDir(ObservationStore store, File dir, LocalDate date, String subject) {
		
	}
	
	
	
	

}
