package astrogeist.scanner.sharpcap;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import astrogeist.scanner.AbstractScanner;
import astrogeist.scanner.Scanner;
import astrogeist.timeline.Timeline;

public final class SharpCapScanner extends AbstractScanner {
	private SharpCapScanner(File root) { super(root); }

	@Override
	public void scan(Timeline timeline) { scanRootDir(timeline, getRootDir()); }

	private static void scanRootDir(Timeline timeline, File dir) {
		var files = dir.listFiles();
		if (files == null) return;

		for (var file : files) {
			if (file.isFile()) continue;

			var date = DateUtils.tryParseDate(file.getName());
			if (date == null) continue;

			scanSubjectsDir(timeline, file, date);
		}
	}

	private static void scanSubjectsDir(Timeline timeline, File dir, LocalDate date) {
		var files = dir.listFiles();
		if (files == null) return;

		for (var file : files) {
			if (file.isFile()) continue;

			var subject = file.getName();
			scanSubjectDir(timeline, file, date, subject);
		}
	}

	private static void scanSubjectDir(Timeline rename, File dir, LocalDate date, String subject) {
		parseCameraSettingsFiles(rename, dir, date, subject);
		parseSerFiles(rename, dir, date, subject);
	}

	private static void parseCameraSettingsFiles(Timeline rename, File dir, LocalDate date, String subject) {
		var files = dir.listFiles();
		if (files == null) return;

		for (var file : files) {
			if (file.isDirectory()) continue;

			if (!file.getName().endsWith(".CameraSettings.txt")) continue;
			parseCameraSettingFile(rename, file, date, subject);
		}
	}

	private static void parseCameraSettingFile(Timeline timeline, File file, LocalDate date, String subject) {
		var time = TimeParser.parseTimeFromFilename(file.getName(), date);
		if (time == null) return;

		timeline.put(time, "Subject", subject);

		var content = CameraSettingParser.parseFile(file);
		timeline.put(time, content);
	}

	private static void parseSerFiles(Timeline rename, File dir, LocalDate date, String subject) {
		var files = dir.listFiles();
		if (files == null) return;

		for (var file : files) {
			if (file.isDirectory()) continue;

			if (!file.getName().endsWith(".ser")) continue;
			parseSerFile(rename, file, date, subject);
		}
	}

	private static void parseSerFile(Timeline rename, File file, LocalDate date, String subject) {
		var time = TimeParser.parseTimeFromFilename(file.getName(), date);
		if (time == null) return;

		rename.put(time, "SerFile", file.getAbsolutePath());
	}
	
	public static Scanner[] createScanners(){
		var retVal = new ArrayList<Scanner>();
		
		var roots = getRoots();
		for (var root : roots) retVal.add(new SharpCapScanner(root));
		
		return retVal.toArray(Scanner.EmptyArray);
	}
	
}
