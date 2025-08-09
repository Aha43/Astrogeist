package astrogeist.engine.abstraction;

import java.nio.file.Path;

import astrogeist.engine.typesystem.Type;

public interface TypeResolver {
	Type resolve(String name, String value);
	Type resolveFileType(Path file);
}
