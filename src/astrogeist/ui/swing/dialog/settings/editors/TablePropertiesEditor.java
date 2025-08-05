package astrogeist.ui.swing.dialog.settings.editors;

import javax.swing.JComponent;

import astrogeist.engine.scanner.NormalizedProperties;
import astrogeist.engine.util.Strings;
import astrogeist.ui.swing.component.selection.SelectionTablePanel;

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
