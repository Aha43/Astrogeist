package astrogeist.ui.swing.dialog.settings;

import java.util.LinkedHashMap;

import astrogeist.engine.setting.SettingKeys;
import astrogeist.ui.swing.dialog.settings.editors.PathListEditor;
import astrogeist.ui.swing.dialog.settings.editors.SettingsEditor;
import astrogeist.ui.swing.dialog.settings.editors.TablePropertiesEditor;
import astrogeist.ui.swing.dialog.settings.editors.TextEditor;

public final class SettingsEditorProvider {
    private static final LinkedHashMap<String, SettingsEditor> _editors = new LinkedHashMap<>();
    
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

