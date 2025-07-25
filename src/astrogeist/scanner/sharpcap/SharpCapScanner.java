package astrogeist.scanner.sharpcap;

import java.io.File;
import java.time.LocalDate;

import astrogeist.scanner.AbstractScanner;
import astrogeist.store.ObservationStore;

public final class SharpCapScanner extends AbstractScanner {
	
	public SharpCapScanner(File root) { super(root); }

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

		store.put(time, "subject", subject);

		var content = CameraSettingParser.parseFile(file);
		store.put(time, "movie:camera", content.get("camera"));
		store.put(time, "movie:binning", content.get("Binning"));
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

		store.put(time, "movie:ser-file", file.getAbsolutePath());
	}

}
