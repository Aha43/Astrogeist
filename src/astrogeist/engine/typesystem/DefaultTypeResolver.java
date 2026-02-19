package astrogeist.engine.typesystem;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import astrogeist.engine.abstraction.TypeResolver;

public final class DefaultTypeResolver implements TypeResolver {
	private final Map<String, Function<String, Type>> rules = Map.of(
		TypeKeywords.FILE.word(),          v -> Type.DiskFile().resolve(v),	
		TypeKeywords.BINNING.word(),       v -> Type.Binning(),
		TypeKeywords.EXPOSURE.word(),      v -> Type.Exposure().resolve(v),
		TypeKeywords.GAIN.word(),          v -> Type.Gain(),
		TypeKeywords.FRAMECOUNT.word(),    v -> Type.FrameCount(),
		TypeKeywords.CONFIGURATION.word(), v -> Type.Configuration()
	);
	
	@Override public final Type resolve(String name, String value) {
		var key = (name == null ? "" : name.trim().toLowerCase(Locale.ROOT));
	    return rules.getOrDefault(key, v -> Type.Text()).apply(value);
	}
	
	@Override public final Type resolveFileType(Path path) { 
		return resolve("file", path.toString()); }
}
