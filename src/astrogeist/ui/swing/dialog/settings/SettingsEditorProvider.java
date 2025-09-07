package astrogeist.ui.swing.dialog.settings;

import java.util.LinkedHashMap;

import astrogeist.engine.abstraction.TimelineNames;
import astrogeist.engine.setting.SettingKeys;
import astrogeist.ui.swing.dialog.settings.editors.PathListEditor;
import astrogeist.ui.swing.dialog.settings.editors.SettingsEditor;
import astrogeist.ui.swing.dialog.settings.editors.TablePropertiesEditor;
import astrogeist.ui.swing.dialog.settings.editors.TextEditor;

public final class SettingsEditorProvider {
    private final LinkedHashMap<String, SettingsEditor> editors = new LinkedHashMap<>();
    
    private final SettingsEditor defaultEditor = new TextEditor();

    public SettingsEditorProvider(TimelineNames timelineNames) {
        this.editors.put(SettingKeys.DATA_ROOTS, new PathListEditor());
        this.editors.put(SettingKeys.TABLE_COLUMNS, new TablePropertiesEditor(timelineNames));
    }

    public SettingsEditor getEditor(String scopedKey) {
        var retVal = this.editors.get(scopedKey);
        return retVal == null ? this.defaultEditor : retVal;
    }
}

