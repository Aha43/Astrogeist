package astrogeist.util;

import java.io.File;

import astrogeist.Common;

public final class FilesUtil {
	public static String getFilenameNoExt(File file) {
		var name = file.getName();
		var dotIndex = name.lastIndexOf('.');
		var baseName = (dotIndex == -1) ? name : name.substring(0, dotIndex);
		return baseName;
	}
	
	private FilesUtil() { Common.throwStaticClassInstantiateError(); }
}
