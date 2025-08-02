package astrogeist.util;

import java.io.File;
import java.nio.file.Path;

import astrogeist.Common;

public final class FilesUtil {
	public static String getBaseName(File file) {
		var name = file.getName();
		var dotIndex = name.lastIndexOf('.');
		var baseName = (dotIndex == -1) ? name : name.substring(0, dotIndex);
		return baseName;
	}
	
	public static String getExtension(File file) {
	    String name = file.getName();
	    int dotIndex = name.lastIndexOf('.');
	    return (dotIndex == -1) ? null : name.substring(dotIndex + 1);
	}
	
	public static String getExtension(Path path) {
	    String name = path.getFileName().toString();
	    int dotIndex = name.lastIndexOf('.');
	    return (dotIndex == -1) ? "" : name.substring(dotIndex + 1);
	}
	
	private FilesUtil() { Common.throwStaticClassInstantiateError(); }
}
