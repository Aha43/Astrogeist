package astrogeist.setting;

import java.util.LinkedHashMap;

import astrogeist.Common;
import astrogeist.app.resources.Resources;
import astrogeist.util.io.NameValueMapXml;

public final class SettingsIo {
    public static final LinkedHashMap<String, String> DEFAULTS = new LinkedHashMap<>();
    
    static {
    	DEFAULTS.put(SettingKeys.DATA_ROOTS, "");
    	DEFAULTS.put(SettingKeys.TABLE_COLUMNS, "");
    }

    public static LinkedHashMap<String, String> loadOrCreate() throws Exception {
    	LinkedHashMap<String, String> props = Resources.getSettingsFile().exists() ? load() : new LinkedHashMap<>();

        // Add missing defaults
        for (var entry : DEFAULTS.entrySet()) {
            props.putIfAbsent(entry.getKey(), entry.getValue());
        }

        // Save updated (ensures valid file)
        save(props);
        return props;
    }

    public static void save(LinkedHashMap<String, String> props) throws Exception {
    	var settingsFile = Resources.getSettingsFile();
    	NameValueMapXml.save(props, settingsFile);
    }
    
    public static void saveGrouped(LinkedHashMap<String, LinkedHashMap<String, String>> groupedProps) throws Exception {
    	LinkedHashMap<String, String> flat = new LinkedHashMap<>();
        for (var groupEntry : groupedProps.entrySet()) {
            String group = groupEntry.getKey();
            for (var entry : groupEntry.getValue().entrySet()) {
                String key = group.equals("general") ? entry.getKey() : group + ":" + entry.getKey();
                flat.put(key, entry.getValue());
            }
        }
        save(flat);
    }

    public static LinkedHashMap<String, LinkedHashMap<String, String>> groupByPrefix(LinkedHashMap<String, String> flat) {
    	LinkedHashMap<String, LinkedHashMap<String, String>> grouped = new LinkedHashMap<>();
        for (var entry : flat.entrySet()) {
            String[] parts = entry.getKey().split(":", 2);
            String group = parts.length == 2 ? parts[0] : "general";
            String key = parts.length == 2 ? parts[1] : parts[0];
            grouped.computeIfAbsent(group, g -> new LinkedHashMap<>()).put(key, entry.getValue());
        }
        return grouped;
    }

    public static LinkedHashMap<String, LinkedHashMap<String, String>> loadGrouped() throws Exception { return groupByPrefix(load()); }

    private static LinkedHashMap<String, String> load() throws Exception {
    	var settingsFile = Resources.getSettingsFile();
    	var retValue = NameValueMapXml.load(settingsFile);
        return retValue;
    }
    
    private SettingsIo() { Common.throwStaticClassInstantiateError(); }
}
