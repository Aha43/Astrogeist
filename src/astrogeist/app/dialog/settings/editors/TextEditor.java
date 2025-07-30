package astrogeist.app.dialog.settings.editors;

import javax.swing.JComponent;
import javax.swing.JTextField;

public final class TextEditor implements SettingsEditor {
	private final JTextField _textField;

    public TextEditor() { _textField = new JTextField(30); }

    @Override public JComponent getEditorComponent(String currentValue) { 
    	_textField.setText(currentValue != null ? currentValue : "");
        return _textField; 
    }
    @Override public String getEditedValue() { return _textField.getText(); }
}
