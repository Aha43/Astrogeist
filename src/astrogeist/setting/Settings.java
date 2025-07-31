package astrogeist.setting;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import astrogeist.Common;
import astrogeist.util.Strings;

public final class Settings {
    private static LinkedHashMap<String, String> current;

    static {
        try {
            current = SettingsIo.loadOrCreate();
        } catch (Exception e) {
            System.err.println("Failed to load settings: " + e.getMessage());
            current = new LinkedHashMap<>(SettingsIo.DEFAULTS);
        }
    }
    
    // I/O
    
    public static void load() throws Exception { current = SettingsIo.loadOrCreate(); }

    public static void save() throws Exception { SettingsIo.save(current); }

    public static LinkedHashMap<String, String> raw() { return current; }
    
    // Set methods
    
    public static void set(String key, String value) { current.put(key, value); }
    
    public static void setCsv(String key, List<String> l) { current.put(key, Strings.toCsv(l)); }
    
    // Get methods
    
    public static String get(String key) {
        return current.getOrDefault(key, SettingsIo.DEFAULTS.get(key));
    }
    
    public static List<String> getCsv(String key) { return Strings.fromCsv(Settings.get(key)); }
    
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
    
    //

    private Settings() { Common.throwStaticClassInstantiateError(); }
}
