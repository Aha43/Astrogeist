package astrogeist.app.dialog.settings.editors;

import javax.swing.JComponent;

import astrogeist.app.component.selection.SelectionTablePanel;
import astrogeist.scanner.NormalizedProperties;
import astrogeist.util.Strings;

public class TablePropertiesEditor implements SettingsEditor {
	private SelectionTablePanel component;
	
	@Override
	public JComponent getEditorComponent(String currentValue) {
		var all = NormalizedProperties.getNormalizedNamesAndUserDataNames();
		var selected = Strings.fromCsv(currentValue);
		this.component = new SelectionTablePanel(selected, all);
		return this.component;
	}

	@Override
	public String getEditedValue() {
		var selected = this.component.getSelectedItems();
		return Strings.toCsv(selected);
	}

}
