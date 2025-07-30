package astrogeist.app.dialog.settings.editors;

import javax.swing.JComponent;

public interface SettingsEditor {
	JComponent getEditorComponent(String currentValue);
    String getEditedValue();
}
