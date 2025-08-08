package astrogeist.engine.typesystem;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import astrogeist.engine.abstraction.TypeResolver;

public final class DefaultTypeResolver implements TypeResolver {
	private final Map<String, Function<String, Type>> rules = Map.of(
		"file",     v -> Type.DiskFile().resolve(v),	
		"binning",  v -> Type.Binning(),
		"exposure", v -> Type.Exposure().resolve(v),
		"gain",     v -> Type.Gain(),
		"framecount", v -> Type.FrameCount()
	);
	
	public Type resolve(String name, String value) {
		var key = (name == null ? "" : name.trim().toLowerCase(Locale.ROOT));
	    return rules.getOrDefault(key, v -> Type.Text()).apply(value);
	}

}
