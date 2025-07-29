package astrogeist.setting;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import astrogeist.Common;

public final class Settings {
    private static Map<String, String> current;

    static {
        try {
            current = SettingsIO.loadOrCreate();
        } catch (Exception e) {
            System.err.println("Failed to load settings: " + e.getMessage());
            current = new LinkedHashMap<>(SettingsIO.DEFAULTS);
        }
    }
    
    public static void load() throws Exception { current = SettingsIO.loadOrCreate(); }

    public static String get(String key) {
        return current.getOrDefault(key, SettingsIO.DEFAULTS.get(key));
    }

    public static void set(String key, String value) { current.put(key, value); }

    public static void save() throws Exception { SettingsIO.save(current); }

    public static Map<String, String> raw() { return current; }
    
    public static List<File> getPaths(String key) {
        var raw = get(key);
        var result = new ArrayList<File>();

        if (raw != null && !raw.isEmpty()) {
            var parts = raw.split(File.pathSeparator);
            for (var part : parts) {
                var trimmed = part.trim();
                if (!trimmed.isEmpty()) result.add(new File(trimmed));
            }
        }

        return result;
    }

    private Settings() { Common.throwStaticClassInstantiateError(); }
}
