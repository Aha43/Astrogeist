package astrogeist.scanner.sharpcap;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import astrogeist.scanner.AbstractScanner;
import astrogeist.scanner.Scanner;
import astrogeist.store.ObservationStore;

public final class SharpCapScanner extends AbstractScanner {
	private SharpCapScanner(File root) { super(root); }

	@Override
	public void scan(ObservationStore store) { scanRootDir(store, getRootDir()); }

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
		parseCameraSettingsFiles(store, dir, date, subject);
		parseSerFiles(store, dir, date, subject);
	}

	private static void parseCameraSettingsFiles(ObservationStore store, File dir, LocalDate date, String subject) {
		var files = dir.listFiles();
		if (files == null) return;

		for (var file : files) {
			if (file.isDirectory()) continue;

			if (!file.getName().endsWith(".CameraSettings.txt")) continue;
			parseCameraSettingFile(store, file, date, subject);
		}
	}

	private static void parseCameraSettingFile(ObservationStore store, File file, LocalDate date, String subject) {
		var time = TimeParser.parseTimeFromFilename(file.getName(), date);
		if (time == null) return;

		store.put(time, "Subject", subject);

		var content = CameraSettingParser.parseFile(file);
		store.put(time, content);
	}

	private static void parseSerFiles(ObservationStore store, File dir, LocalDate date, String subject) {
		var files = dir.listFiles();
		if (files == null) return;

		for (var file : files) {
			if (file.isDirectory()) continue;

			if (!file.getName().endsWith(".ser")) continue;
			parseSerFile(store, file, date, subject);
		}
	}

	private static void parseSerFile(ObservationStore store, File file, LocalDate date, String subject) {
		var time = TimeParser.parseTimeFromFilename(file.getName(), date);
		if (time == null) return;

		store.put(time, "SerFile", file.getAbsolutePath());
	}
	
	public static Scanner[] createScanners(){
		var retVal = new ArrayList<Scanner>();
		
		var roots = getRoots();
		for (var root : roots) retVal.add(new SharpCapScanner(root));
		
		return retVal.toArray(Scanner.EmptyArray);
	}
	
}
