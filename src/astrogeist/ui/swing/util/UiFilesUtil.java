package astrogeist.ui.swing.util;

import java.io.File;
import java.nio.file.Path;

import astrogeist.common.Common;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class UiFilesUtil {
	private UiFilesUtil() { Common.throwStaticClassInstantiateError(); }
	
	public static void openFile(String path) { openFile(Path.of(path)); }
	public static void openFile(Path path) { openFile(path.toFile()); }
	
	public static void openFile(File file) {
		try {
			java.awt.Desktop.getDesktop().open(file);
		} catch (Exception x) {
			MessageDialogs.showError("Failed to open file: ", x);
		}
	}
	
}
