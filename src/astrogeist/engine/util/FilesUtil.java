package astrogeist.engine.util;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import astrogeist.common.Common;
import astrogeist.engine.typesystem.Type;

public final class FilesUtil {
	private FilesUtil() { Common.throwStaticClassInstantiateError(); }
	
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
	
	public static List<Path> getRegularFilePaths(Path dir) throws IOException {
		try (var stream = Files.find(
				dir,
		        Integer.MAX_VALUE,
		        (p, a) -> a.isRegularFile() && !isHidden(p))) {
		      return stream.collect(Collectors.toList());
		    }
	}
	
	public static boolean isHidden(Path file) { 
		return file.getFileName().toString().startsWith("."); }
	
	public static boolean existsAs(String path, boolean folder) {
		return existsAs(Path.of(path), folder); }
	
	public static boolean existsAs(Path path, boolean folder) {
		if (!Files.exists(path)) return false;
		if (folder) return Files.isDirectory(path);
		return Files.isRegularFile(path);
	}
	
}
