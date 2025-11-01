package astrogeist.engine.persitence.settings;

import java.util.LinkedHashMap;
import java.util.List;

import aha.common.Guards;
import aha.common.Strings;

public final class Settings {
	public static final String TABLE_COLUMNS="ui:table-columns";
	
	private final LinkedHashMap<String, String> data;
	
	public Settings() { this.data = new LinkedHashMap<String, String>(); }
	
	public Settings(LinkedHashMap<String, String> data) { this.data = data; }
	
	public final String get(String key) {
		Guards.requireNonEmpty(key, "key");
		var retVal = this.data.get(key);
		if (retVal != null) return retVal;
		throw new IllegalArgumentException("value for key : '" + key + "' not found");
	}
	
	public final void set(String key, String value) {
		Guards.requireNonEmpty(key, "key");
		Guards.requireNonEmpty(key, "value");
		this.data.put(key, value);
	}
	
	public final void remove(String key) {
		Guards.requireNonEmpty(key, "key");
		this.data.remove(key);
	}
	
	public final LinkedHashMap<String, String> data() { return this.data; }
	
	public final void clear() { this.data.clear(); }
	
	public final List<String> getCsv(String key) { return Strings.fromCsv(this.get(key)); }
	
	public final LinkedHashMap<String, LinkedHashMap<String, String>> groupByPrefix() {
    	LinkedHashMap<String, LinkedHashMap<String, String>> grouped = new LinkedHashMap<>();
        for (var entry : this.data.entrySet()) {
            String[] parts = entry.getKey().split(":", 2);
            String group = parts.length == 2 ? parts[0] : "general";
            String key = parts.length == 2 ? parts[1] : parts[0];
            grouped.computeIfAbsent(group, g -> new LinkedHashMap<>()).put(key, entry.getValue());
        }
        return grouped;
    }
	
    public final void setGrouped(LinkedHashMap<String, LinkedHashMap<String, String>> groupedProps) throws Exception {
    	this.clear();
    	for (var groupEntry : groupedProps.entrySet()) {
    		String group = groupEntry.getKey();
    		for (var entry : groupEntry.getValue().entrySet()) {
    			String key = group.equals("general") ? entry.getKey() : group + ":" + entry.getKey();
    			this.set(key, entry.getValue());
    		}
    	}
    }
      
}
	
