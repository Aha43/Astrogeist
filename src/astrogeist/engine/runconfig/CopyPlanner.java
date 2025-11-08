package astrogeist.engine.runconfig;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import aha.common.util.Guards;

public final class CopyPlanner {
	private CopyPlanner() { Guards.throwStaticClassInstantiateError(); }
	
    static final class Result { 
    	final int totalMatches; final List<String> samples;
        Result(int n, List<String> s){ this.totalMatches=n; this.samples=s; } 
    }

    public final static Result preview(
    	String fromDir,
    	List<String> includeGlobs,
    	List<String> excludeGlobs,
    	int sampleLimit) throws Exception {
        
    	Path dir = Paths.get(fromDir);
        if (!Files.isDirectory(dir)) 
        	return new Result(0, List.of("(missing dir)"));

        List<PathMatcher> inc = toMatchers(includeGlobs);
        List<PathMatcher> exc = toMatchers(excludeGlobs);

        int count = 0;
        List<String> samples = new ArrayList<>();
        try (var stream = Files.list(dir)) {
            for (Path p : (Iterable<Path>) stream::iterator) {
                if (!Files.isRegularFile(p)) continue;
                Path name = p.getFileName();
                if (name == null) continue;
                if (!matchesAny(name, inc)) continue;
                if (matchesAny(name, exc)) continue;
                count++;
                if (samples.size() < sampleLimit) samples.add(p.toString());
            }
        }
        return new Result(count, samples);
    }

    private static List<PathMatcher> toMatchers(List<String> globs) {
        var fs = FileSystems.getDefault();
        List<PathMatcher> out = new ArrayList<>();
        for (String g : globs) out.add(fs.getPathMatcher("glob:" + g));
        return out;
    }
    private static boolean matchesAny(Path name, List<PathMatcher> ms) {
        for (PathMatcher m : ms) if (m.matches(name)) return true;
        return false;
    }
    
}
