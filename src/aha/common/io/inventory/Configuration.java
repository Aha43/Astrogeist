package aha.common.io.inventory;

import static aha.common.guard.Guards.requireNonEmpty;
import static aha.common.util.Strings.quote;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Configuration {
	
	private final Map<String, List<String>> items = new LinkedHashMap<>();
	
	public Configuration add(String part, String item) {
		requireNonEmpty(part, "part");
		
		var list = new ArrayList<String>();
		list.add(item);
		this.items.put(part, list);
		return this;
	}
	
	public Configuration add(String name, List<String> items) {
		this.items.put(name, new ArrayList<String>(items));
		return this;
	}
	
	public Set<String> parts(){ return this.items.keySet(); }
	
	public List<String> part(String part) { 
		requireNonEmpty(part, "part");
		if (!this.items.containsKey(part))
			throw new IllegalArgumentException("Part : " + quote(part) +
				" not found");
		var l = this.items.get(part);
		return l;
		
	}

}
