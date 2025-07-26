package astrogeist.app.dialog.settings;

import java.util.HashMap;
import java.util.Map;

public final class SettingsEditorProvider {
    private static final Map<String, SettingsEditor> _editors = new HashMap<>();
    
    private static final SettingsEditor _defaultEditor = new TextEditor();

    static {
        // Register editors here
        _editors.put("scanner:roots", new PathListEditor());
        //register("center:columnsToShow", ColumnPickerEditor::new);
        // fallback to text
    }

    public static SettingsEditor getEditor(String scopedKey) {
        var retVal = _editors.get(scopedKey);
        return retVal == null ? _defaultEditor : retVal;
    }
}

