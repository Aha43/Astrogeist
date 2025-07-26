package astrogeist.app.dialog.settings;

import javax.swing.JComponent;

public interface SettingsEditor {
	JComponent getEditorComponent(String currentValue);
    String getEditedValue();
}
