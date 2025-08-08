package astrogeist.engine.timeline;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import astrogeist.engine.abstraction.TimelineValuePool;
import astrogeist.engine.abstraction.TypeResolver;

public final class DefaultTimelineValuePool implements TimelineValuePool {
	private final TypeResolver typeResolver;
	
	private final ConcurrentMap<PoolKey, TimelineValue> pool = new ConcurrentHashMap<>();
	
	public DefaultTimelineValuePool(TypeResolver typeResolver) { this.typeResolver = typeResolver; }
	
	public TimelineValue get(String name, String value) {
		var type = this.typeResolver.resolve(name, value);
		var canon = value == null ? "" : value.trim(); // maybe more canonicalization later
	    var key = new PoolKey(type, canon);
	    return this.pool.computeIfAbsent(key, k -> new TimelineValue(k.value(), k.type()));
	}
}
