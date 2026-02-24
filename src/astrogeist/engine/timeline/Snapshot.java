package astrogeist.engine.timeline;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import astrogeist.engine.typesystem.Type;

/**
 * <p>
 *   Observation data captured at a given time.
 * </p>
 */
public final class Snapshot {
	/**
	 * <p>
	 *   The empty instance shared.
	 * </p>
	 */
	public static Snapshot Empty = new Snapshot();
	
	private final Map<String, TimelineValue> snap;
	
	// Creates the shared single empty snapshot EMPTY.
	private Snapshot() { this.snap = Map.of(); }
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param snap Content at a observation time. Keys are names (i.e. exposure,
	 *             gain etc.) and values are 
	 *             {@link TimelineValue} objects representing the named and 
	 *             {@link Type typed} observation value.
	 */
	public Snapshot(Map<String, TimelineValue> snap) {
		this.snap = requireNonNull(snap, "snap"); }
	
	/**
	 * <p>
	 *   Get as read only snapshot map. Keys are names (i.e. exposure,
	 *   gain etc.) and values are 
	 *   {@link TimelineValue} objects representing the named and 
	 *   {@link Type typed} observation value.  
	 * </p>
	 * @return the map.
	 */
	public final Map<String, TimelineValue> asMap() { 
		return Map.copyOf(this.snap); }
	
	public final Map<String, TimelineValue> asMapWithType(Type type) {
		requireNonNull(type, "type");
		return this.asMapFiltered(tlv -> Type.isA(tlv.type(), type)); 
	}
	
	public final Map<String, TimelineValue> asMapWithoutType(Type type) {
		requireNonNull(type, "type");
		return this.asMapFiltered(tlv -> !Type.isA(tlv.type(), type)); 
	}
	
	public final Map<String, TimelineValue> asMapFiltered(
		Predicate<TimelineValue> predicate) {
		
		requireNonNull(predicate, "predicate");
		var retVal = new LinkedHashMap<String, TimelineValue>();
		for (var curr : this.snap.entrySet()) {
			var tlv = curr.getValue();
			if (predicate.test(tlv)) retVal.put(curr.getKey(), tlv);
		}
		return Map.copyOf(retVal);
	}
	
	public final TimelineValue value(String name) { 
		var v = this.snap.get(requireNonEmpty(name, "name"));
		return v == null ? TimelineValue.Empty : v;
	}
	
	public final String valueAsString(String name) {
		var tlv = this.value(requireNonEmpty(name, "name"));
		return tlv == null ? null : tlv.value();
	}
	
	public final List<TimelineValue> getOfType(Type type){
		requireNonNull(type, "type");
		var retVal = this.snap.values().stream().filter(
			v -> Type.isA(v.type(), type)).collect(Collectors.toList()); 
		return retVal;
	}
		
	@Override public final String toString() { return this.snap.toString(); }
	
}
