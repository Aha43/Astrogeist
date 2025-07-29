package astrogeist.setting;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import astrogeist.Common;
import astrogeist.app.resources.Resources;

public final class SettingsIO {
    public static final Map<String, String> DEFAULTS = Map.ofEntries(
        Map.entry("ui:columns", "Time,subject,scope,exposure"),
        Map.entry(SettingKeys.DATA_ROOTS, "")
        // Add more defaults here
    );

    public static Map<String, String> loadOrCreate() throws IOException {
        Map<String, String> props = Resources.getSettingsFile().exists() ? load() : new LinkedHashMap<>();

        // Add missing defaults
        for (var entry : DEFAULTS.entrySet()) {
            props.putIfAbsent(entry.getKey(), entry.getValue());
        }

        // Save updated (ensures valid file)
        save(props);
        return props;
    }

    public static void save(Map<String, String> props) throws IOException {
        List<String> lines = new ArrayList<>();
        for (var entry : props.entrySet()) {
            lines.add(entry.getKey());
            lines.add(entry.getValue());
        }
        Files.write(Resources.getSettingsFile().toPath(), lines);
    }
    
    public static void saveGrouped(Map<String, Map<String, String>> groupedProps) throws IOException {
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

    public static Map<String, Map<String, String>> loadGrouped() throws IOException {
        return groupByPrefix(load());
    }

    private static Map<String, String> load() throws IOException {
    	var settingsFile = Resources.getSettingsFile();
        List<String> lines = Files.readAllLines(settingsFile.toPath());
        Map<String, String> result = new LinkedHashMap<>();
        for (int i = 0; i < lines.size() - 1; i += 2) {
            String key = lines.get(i).trim();
            String value = lines.get(i + 1).trim();
            if (!key.isEmpty()) {
                result.put(key, value);
            }
        }
        return result;
    }
    
    private SettingsIO() { Common.throwStaticClassInstantiateError(); }
}


