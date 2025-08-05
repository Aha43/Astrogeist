package astrogeist.ui.swing.dialog.settings.editors;

import javax.swing.JComponent;
import javax.swing.JTextField;

public final class TextEditor implements SettingsEditor {
	private final JTextField textField;

    public TextEditor() { this.textField = new JTextField(30); }

    @Override public JComponent getEditorComponent(String currentValue) { 
    	this.textField.setText(currentValue != null ? currentValue : "");
        return this.textField; 
    }
    @Override public String getEditedValue() { return this.textField.getText(); }
}
