package astrogeist.engine.abstraction;

import astrogeist.engine.typesystem.Type;

public interface TypeResolver {
	Type resolve(String name, String value);
}
