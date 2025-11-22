package astrogeist.engine.appdata.settings;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aha.common.util.AttributeBase;

import static aha.common.util.Strings.fromCsv;;

public final class Settings extends AttributeBase<Settings> {
	public static final String TABLE_COLUMNS="ui:table-columns";
	
	public Settings() {} 
	
	public Settings(Map<String, String> data) { super(data); } 
	
	public final List<String> getCsv(String key, String def) {
		var s = super.getAsString(key, def);
		return fromCsv(s);
	}
	
	public final List<String> getCsv(String key) {
		var s = super.getAsString(key);
		return fromCsv(s);
	}
	
	public final LinkedHashMap<String, LinkedHashMap<String, String>>
		groupByPrefix() {
		
		var data = super.asMap();
		
    	LinkedHashMap<String, LinkedHashMap<String, String>> grouped =
    		new LinkedHashMap<>();
        
    	for (var entry : data.entrySet()) {
            String[] parts = entry.getKey().split(":", 2);
            var group = parts.length == 2 ? parts[0] : "general";
            var key = parts.length == 2 ? parts[1] : parts[0];
            grouped.computeIfAbsent(group,
            	g -> new LinkedHashMap<>()).put(key, 
            		entry.getValue().toString());
        }
        return grouped;
    }
	
    public final void setGrouped(
    	LinkedHashMap<String, LinkedHashMap<String, String>> groupedProps)
    		throws Exception {
    	
    	this.clear();
    	for (var groupEntry : groupedProps.entrySet()) {
    		String group = groupEntry.getKey();
    		for (var entry : groupEntry.getValue().entrySet()) {
    			var key = group.equals("general") ? entry.getKey() : group +
    				":" + entry.getKey();
    			this.set(key, entry.getValue());
    		}
    	}
    }
      
}
