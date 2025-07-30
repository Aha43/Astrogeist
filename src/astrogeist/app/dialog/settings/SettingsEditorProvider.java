package astrogeist.app.dialog.settings;

import java.util.HashMap;
import java.util.Map;

import astrogeist.app.dialog.settings.editors.PathListEditor;
import astrogeist.app.dialog.settings.editors.SettingsEditor;
import astrogeist.app.dialog.settings.editors.TablePropertiesEditor;
import astrogeist.app.dialog.settings.editors.TextEditor;
import astrogeist.setting.SettingKeys;

public final class SettingsEditorProvider {
    private static final Map<String, SettingsEditor> _editors = new HashMap<>();
    
    private static final SettingsEditor _defaultEditor = new TextEditor();

    static {
        _editors.put(SettingKeys.DATA_ROOTS, new PathListEditor());
        _editors.put(SettingKeys.TABLE_COLUMNS, new TablePropertiesEditor());
    }

    public static SettingsEditor getEditor(String scopedKey) {
        var retVal = _editors.get(scopedKey);
        return retVal == null ? _defaultEditor : retVal;
    }
}

