package astrogeist.engine.observatory;

import static aha.common.guard.CollectionGuards.requireKeyNotExists;
import static aha.common.util.Strings.padding;
import static java.lang.System.lineSeparator;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import aha.common.collection.IndexedMap;

public final class Configuration {
	private final InventoryNode root;
	
	private final String code;
	
	private final IndexedMap<String, ObservatorySystem> systems = 
		new IndexedMap<>();
	
	public Configuration(String code, InventoryNode root) {
		this.code = requireNonNull(code, "code");
		this.root = requireNonNull(root, "root"); 
	}
	
	public final String code() { return this.code; }
	
	public final Configuration addSystem(String systemName) {
		return this.addSystem(systemName, null); }
	
	public final Configuration addSystem(String systemName,
		String displayName) {	
		
		requireKeyNotExists(systemName, this.systems, "systemName");
		var system = root.system(systemName, displayName);
		this.systems.put(systemName, system);
		return this;
	}
	
	public final int size() { return this.systems.size(); }
	
	public final ObservatorySystem getByName(String name) { 
		return this.systems.get(name); }
	
	public final ObservatorySystem getByIndex(int idx) {
		return this.systems.getAt(idx); }
	
	public final int indexOf(ObservatorySystem o) { 
		return this.systems.indexOf(o); }
	
	public final List<String> names(){
		return new ArrayList<>(this.systems.keySet()); }
	
	@Override public final String toString() {
		var ls = lineSeparator();
		var stbu = new StringBuilder();
		for (var e : this.systems.entrySet())
			stbu.append(padding(2))
				.append(e.getKey())
				.append(ls)
				.append(padding(4))
				.append(e.getValue())
				.append(ls);
		return stbu.toString();
	}
	
}
