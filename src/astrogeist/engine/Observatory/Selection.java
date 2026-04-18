package astrogeist.engine.observatory;

import static aha.common.guard.CollectionGuards.requireKeyNotExists;
import static java.util.Objects.requireNonNull;
import static java.lang.System.lineSeparator;

import java.util.ArrayList;

import static java.util.List.copyOf;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aha.common.abstraction.IdNames;
import astrogeist.engine.timeline.Snapshot;
import astrogeist.engine.typesystem.Type;

/**
 * <p>
 *   Holds a selection of
 *   {@link Configuration} each from a different
 *   {@link Axis}.
 * </p>
 */
public final class Selection {
	private final Map<String, Configuration> configurations =
		new LinkedHashMap<>(); 
	
	private final IdNames idNames;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param idNames the map from id to names (for end user).
	 */
	public Selection(IdNames idNames) { 
		this.idNames = requireNonNull(idNames, "idNames"); }
	
	/**
	 * <p>
	 *   Copy constructor.
	 * </p>
	 * @param o {@link Selection} to copy.
	 */
	public Selection(Selection o) {
		requireNonNull(o, "o");
		this.idNames = o.idNames;
		this.configurations.putAll(o.configurations);
	}
	
	public Selection(IdNames idNames, Snapshot snapshot, Observatory o) {
		requireNonNull(idNames, "idNames");
		requireNonNull(snapshot, "snapshot");
		requireNonNull(o, "o");
		
		this.idNames = idNames;
		
		var tlvs = snapshot.asMapWithType(Type.Configuration());
		for (var e : tlvs.entrySet()) {
			var axisId = e.getKey();
			var configId = e.getValue().value();
			var axis = o.getAxisById(axisId);
			if (axis != null) {
				var config = axis.getConfigurationById(configId);
				if (config != null) this.add(config);
			}
		}
	}
	
	/**
	 * <p>
	 *   Adds
	 *   {@link Configuration} to the selection.
	 * </p>
	 * @param configuration {@link Configuration} to add.
	 * @throws NullPointerException if {@code configuration == null}.
	 * @throws IllegalArgumentException if a
	 *         {@link Configuration} already added that is in same
	 *         {@link Axis} as {@code configuration}. 
	 */
	public final void add(Configuration configuration) {
		var axis = requireNonNull(configuration, "configuration").axis();
		var key = requireKeyNotExists(axis.id(), this.configurations);
		this.configurations.put(key, configuration);
	}
	
	/**
	 * <p>
	 *   Tells if this is empty.
	 * </p>
	 * @return {@code true} if has no
	 *         {@link Configuration} else {@code false}.
	 */
	public final boolean isEmpty() { return this.configurations.isEmpty(); }
	
	/**
	 * <p>
	 *   Gets the id values for all
	 *   {@link Configuration}s.
	 * </p>
	 * @return a read only list of ids. 
	 */
	public final List<String> getAxisIds() {
		return copyOf(this.configurations.keySet()); }
	
	/**
	 * <p>
	 *   Gets the names for all
	 *   {@link Configuration}s.   
	 * </p>
	 * @return a read only list of names.
	 */
	public final List<String> getAxisNames() {
		var retVal = new ArrayList<String>();
		for (var curr : this.configurations.keySet()) 
			retVal.add(this.idNames.requireName(curr));
		return copyOf(retVal); 
	}
	
	/**
	 * <p>
	 *   Gets
	 *   {@link Configuration} given the id of the
	 *   {@link Axis} it is in.
	 * </p>
	 * @param axisId the id of the 
	 *               {@link Axis} the
	 *               {@link Configuration} is in.
	 * @return {@link Configuration} or {@code null} if not found.
	 */
	public final Configuration getConfigurationById(String axisId) {
		return this.configurations.get(requireNonNull(axisId, "axisId")); }
	
	/**
	 * <p>
	 *   Gets all
	 *   {@link Configuration}s of selection.
	 * </p>
	 * @return read only list with the
	 *         {@link Configuration}.
	 */
	public final List<Configuration> getConfigurations() {
		return copyOf(this.configurations.values()); }
	
	@Override public final String toString() {
		var ls = lineSeparator();
		var sb = new StringBuilder();
		for (var e : this.configurations.entrySet()) {
			sb.append(e.getKey())
				.append(ls);
		}
		return sb.toString();
	}
	
}
