package astrogeist.engine.timeline;

import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import astrogeist.engine.abstraction.TypeResolver;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.typesystem.Type;

public final class DefaultTimelineValuePool implements TimelineValuePool {
	private final TypeResolver typeResolver;
	
	private final ConcurrentMap<PoolKey, TimelineValue> pool =
		new ConcurrentHashMap<>();
	
	public DefaultTimelineValuePool(TypeResolver typeResolver) { 
		this.typeResolver = typeResolver; }
	
	@Override public final TimelineValue get(String name, String value) {
		var type = this.typeResolver.resolve(name, value);
		return get(type, value);
	}

	@Override public final TimelineValue getFileValue(Path path) {
		path = path.toAbsolutePath().normalize();
		var type = this.typeResolver.resolveFileType(path);
		return get(type, path.toString());
	}
	
	@Override public final TimelineValue get(Type type, String value) {
		// maybe more canonicalization later of canon
		var canon = value == null ? "" : value.trim();
		var key = new PoolKey(type, canon);
	    var retVal = this.pool.computeIfAbsent(
	    	key, 
	    	k -> new TimelineValue(k.value(), k.type()));
	    return retVal;
	}
	
}
