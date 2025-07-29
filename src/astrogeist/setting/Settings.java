package astrogeist.setting;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import astrogeist.Common;

public final class Settings {
    private static Map<String, String> current;

    static {
        try {
            current = SettingsIO.loadOrCreate();
        } catch (IOException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
            current = new LinkedHashMap<>(SettingsIO.DEFAULTS);
        }
    }
    
    public static void load() throws IOException { current = SettingsIO.loadOrCreate(); }

    public static String get(String key) {
        return current.getOrDefault(key, SettingsIO.DEFAULTS.get(key));
    }

    public static void set(String key, String value) { current.put(key, value); }

    public static void save() throws IOException { SettingsIO.save(current); }

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
