package astrogeist.engine.observatory;

import static java.util.Objects.requireNonNull;
import static aha.common.guard.CollectionGuards.requireKeyNotExists;
import static aha.common.guard.StringGuards.requireNonEmpty;

import java.util.List;

import aha.common.abstraction.IdNames;
import aha.common.util.NamedList;

public class Observatory {
	
	private final IdNames idNames;
	
	private final NamedList<Axis> axises = new NamedList<>(Axis::id);
	
	private final String name;
	
	public Observatory(IdNames idNames) { this("Unnamed", idNames); }
	
	public Observatory(String name, IdNames idNames) {
		this.idNames = requireNonNull(idNames, "idNames");
		this.name = requireNonEmpty(name, "name"); 
	}
	
	public final String name() { return this.name; }
	
	// - Axis -
	
	public final Axis getAxisById(String name) {
		return this.axises.get(requireNonEmpty(name, "name")); }
	
	public final List<String> getAxisNames() { return this.axises.names(); }
	
	public final List<Axis> getAxises() { 
		var retVal = this.axises.values();
		return retVal;
	}
	
	public final int getAxisCount() { return this.axises.size(); }
	
	public final int getIndexOfAxis(Axis axis) { 
		return this.axises.indexOf(axis); }
	
	public final Axis newAxis(String id, String name) {
		requireKeyNotExists(id, this.axises, "id");
		var retVal = new Axis(id, name, this);
		this.axises.add(retVal);
		this.idNames.register(retVal);
		return retVal;
	}
	
	// - Overrides -
	
	@Override public final String toString() {
//		var ls = System.lineSeparator();
//		var sb = new StringBuilder();
//		sb.append("name : ").append(quote(this.name()));
//		if (!this.items.isEmpty()) {
//			sb.append(ls).append("Items :");
//			for (var ins : this.items)
//				sb.append(ls).append(padding(4)).append(ins.name());
//		}
//		sb.append(this.defaultAxis());
//		return sb.toString();
		return "";
	}
	
}
