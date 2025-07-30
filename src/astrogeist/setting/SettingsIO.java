package astrogeist.setting;

import java.util.LinkedHashMap;
import java.util.Map;

import astrogeist.Common;
import astrogeist.app.resources.Resources;

public final class SettingsIO {
    public static final Map<String, String> DEFAULTS = Map.ofEntries(
        Map.entry(SettingKeys.DATA_ROOTS, ""),
        Map.entry(SettingKeys.TABLE_COLUMNS, "")
    );

    public static Map<String, String> loadOrCreate() throws Exception {
        Map<String, String> props = Resources.getSettingsFile().exists() ? load() : new LinkedHashMap<>();

        // Add missing defaults
        for (var entry : DEFAULTS.entrySet()) {
            props.putIfAbsent(entry.getKey(), entry.getValue());
        }

        // Save updated (ensures valid file)
        save(props);
        return props;
    }

    public static void save(Map<String, String> props) throws Exception {
    	var settingsFile = Resources.getSettingsFile();
    	SettingsXml.saveSettings(props, settingsFile);
    }
    
    public static void saveGrouped(Map<String, Map<String, String>> groupedProps) throws Exception {
        Map<String, String> flat = new LinkedHashMap<>();
        for (var groupEntry : groupedProps.entrySet()) {
            String group = groupEntry.getKey();
            for (var entry : groupEntry.getValue().entrySet()) {
                String key = group.equals("general") ? entry.getKey() : group + ":" + entry.getKey();
                flat.put(key, entry.getValue());
            }
        }
        save(flat);
    }

    public static Map<String, Map<String, String>> groupByPrefix(Map<String, String> flat) {
        Map<String, Map<String, String>> grouped = new LinkedHashMap<>();
        for (var entry : flat.entrySet()) {
            String[] parts = entry.getKey().split(":", 2);
            String group = parts.length == 2 ? parts[0] : "general";
            String key = parts.length == 2 ? parts[1] : parts[0];
            grouped.computeIfAbsent(group, g -> new LinkedHashMap<>()).put(key, entry.getValue());
        }
        return grouped;
    }

    public static Map<String, Map<String, String>> loadGrouped() throws Exception {
        return groupByPrefix(load());
    }

    private static Map<String, String> load() throws Exception {
    	var settingsFile = Resources.getSettingsFile();
    	var retValue = SettingsXml.loadSettings(settingsFile);
        return retValue;
    }
    
    private SettingsIO() { Common.throwStaticClassInstantiateError(); }
}
