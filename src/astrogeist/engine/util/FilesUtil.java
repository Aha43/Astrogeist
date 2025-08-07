package astrogeist.engine.util;

import java.util.List;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import astrogeist.Common;
import astrogeist.engine.typesystem.Type;

public final class FilesUtil {
	public static String getBaseName(File file) { return getBaseName(file.getName()); }
	public static String getBaseName(Path path) { return getBaseName(path.getFileName().toString()); }
	
	private static String getBaseName(String name) {
		int dotIndex = name.lastIndexOf('.');
		var baseName = (dotIndex == -1) ? name : name.substring(0, dotIndex);
		return baseName;
	}
	
	public static String getExtension(File file) {
	    var name = file.getName();
	    int dotIndex = name.lastIndexOf('.');
	    return (dotIndex == -1) ? null : name.substring(dotIndex + 1);
	}
	
	public static String getExtension(Path path) {
	    var name = path.getFileName().toString();
	    var dotIndex = name.lastIndexOf('.');
	    return (dotIndex == -1) ? "" : name.substring(dotIndex + 1);
	}
	
	public static List<File> pathsToFiles(List<Path> paths) { 
		return paths.stream().map(Path::toFile).toList(); }
	
	public static List<File> stringsToFiles(List<String> paths) { 
		return paths.stream().map(File::new).toList(); }
	
	public static List<Path> filesToPaths(List<File> paths) { 
		return paths.stream().map(File::toPath).toList(); }
	
	public static Map<Type.DiskFile, List<File>> groupByExtension(List<File> files) {
        Map<Type.DiskFile, List<File>> grouped = new LinkedHashMap<>();

        for (var file : files) {
            if (!file.isFile()) continue;
            var type = Type.DiskFile().resolve(file);
            grouped.computeIfAbsent(type, k -> new ArrayList<>()).add(file);
        }

        return grouped;
    }
	
	private FilesUtil() { Common.throwStaticClassInstantiateError(); }
}
