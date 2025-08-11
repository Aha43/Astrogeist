package astrogeist.engine.timeline;

import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import astrogeist.engine.abstraction.TimelineValuePool;
import astrogeist.engine.abstraction.TypeResolver;
import astrogeist.engine.typesystem.Type;

public final class DefaultTimelineValuePool implements TimelineValuePool {
	private final TypeResolver typeResolver;
	
	private final ConcurrentMap<PoolKey, TimelineValue> pool = new ConcurrentHashMap<>();
	
	public DefaultTimelineValuePool(TypeResolver typeResolver) { this.typeResolver = typeResolver; }
	
	@Override public final TimelineValue get(String name, String value) {
		var type = this.typeResolver.resolve(name, value);
		return get(value, type);
	}

	@Override public final TimelineValue getFileValue(Path path) {
		path = path.toAbsolutePath().normalize();
		var type = this.typeResolver.resolveFileType(path);
		return get(path.toString(), type);
	}
	
	private final TimelineValue get(String value, Type type) {
		var canon = value == null ? "" : value.trim(); // maybe more canonicalization later
	    var key = new PoolKey(type, canon);
	    return this.pool.computeIfAbsent(key, k -> new TimelineValue(k.value(), k.type()));
	}
}
