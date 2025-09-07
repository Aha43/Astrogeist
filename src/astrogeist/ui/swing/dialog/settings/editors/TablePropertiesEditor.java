package astrogeist.ui.swing.dialog.settings.editors;

import javax.swing.JComponent;

import astrogeist.engine.abstraction.TimelineNames;
import astrogeist.engine.util.Strings;
import astrogeist.ui.swing.component.selection.SelectionTablePanel;

public final class TablePropertiesEditor implements SettingsEditor {
	private TimelineNames timelineNames;
	
	private SelectionTablePanel component;
	
	public TablePropertiesEditor(TimelineNames timelineNames) {
		this.timelineNames = timelineNames;
	}
	
	@Override public JComponent getEditorComponent(String currentValue) {
		var all = this.timelineNames.getTimelineNames();
		var selected = Strings.fromCsv(currentValue);
		this.component = new SelectionTablePanel(selected, all);
		return this.component;
	}

	@Override public String getEditedValue() {
		var selected = this.component.getSelectedItems();
		return Strings.toCsv(selected);
	}

}
