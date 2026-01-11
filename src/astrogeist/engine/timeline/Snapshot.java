package astrogeist.engine.timeline;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	 *             {@link TimelineValue} objects representing the named
	 *             observation value. 
	 */
	public Snapshot(Map<String, TimelineValue> snap) { this.snap = snap; }
	
	
	public Map<String, TimelineValue> asMap(){
		return new HashMap<>(this.snap); }
	
	public TimelineValue value(String name) { 
		var v = this.snap.get(name);
		return v == null ? TimelineValue.Empty : v;
	}
	
	public String valueAsString(String name) {
		var tlv = this.value(name);
		return tlv == null ? null : tlv.value();
	}
	
	public List<TimelineValue> getOfType(Type type){
		var retVal = this.snap.values().stream().filter(
			v -> Type.isA(v.type(), type)).collect(Collectors.toList()); 
		return retVal;
	}
		
	public Map<String, TimelineValue> getExcludingTypeMap(Type type) {
		Map<String, TimelineValue> result = new LinkedHashMap<>();
		for (Entry<String, TimelineValue> entry : this.snap.entrySet()) {
			if (!Type.isA(entry.getValue().type(), type))
				result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
}
