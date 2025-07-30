package astrogeist.app.dialog.settings.editors;

import javax.swing.JComponent;

import astrogeist.app.component.selection.SelectionTablePanel;
import astrogeist.scanner.NormalizedProperties;
import astrogeist.util.Strings;

public class TablePropertiesEditor implements SettingsEditor {
	private SelectionTablePanel _component;
	
	@Override
	public JComponent getEditorComponent(String currentValue) {
		var all = NormalizedProperties.getNormalizedNames();
		var selected = Strings.fromCsv(currentValue);
		_component = new SelectionTablePanel(selected, all);
		return _component;
	}

	@Override
	public String getEditedValue() {
		var selected = _component.getSelectedItems();
		return Strings.toCsv(selected);
	}

}
