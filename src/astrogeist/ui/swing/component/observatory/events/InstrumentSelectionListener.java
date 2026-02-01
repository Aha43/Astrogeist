package astrogeist.ui.swing.component.observatory.events;

import java.util.List;

public interface InstrumentSelectionListener {
	void selectionChanged(List<String> selectedInstrumentNames);
}
