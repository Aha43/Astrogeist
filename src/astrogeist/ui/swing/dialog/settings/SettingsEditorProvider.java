package astrogeist.ui.swing.dialog.settings;

import java.util.LinkedHashMap;

import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.engine.persitence.settings.Settings;
import astrogeist.ui.swing.dialog.settings.editors.SettingsEditor;
import astrogeist.ui.swing.dialog.settings.editors.TablePropertiesEditor;
import astrogeist.ui.swing.dialog.settings.editors.TextEditor;

public final class SettingsEditorProvider {
    private final LinkedHashMap<String, SettingsEditor> editors = new LinkedHashMap<>();
    
    private final SettingsEditor defaultEditor = new TextEditor();

    public SettingsEditorProvider(TimelineNames timelineNames) {
        this.editors.put(Settings.TABLE_COLUMNS, new TablePropertiesEditor(timelineNames));
    }

    public SettingsEditor getEditor(String scopedKey) {
        var retVal = this.editors.get(scopedKey);
        return retVal == null ? this.defaultEditor : retVal;
    }
}

