package astrogeist.app.toolbar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import astrogeist.app.component.ObservationTablePanel;
import astrogeist.scanner.CompositeScanner;
import astrogeist.store.ObservationStore;

public final class ToolBarFactory {
	
	public static JToolBar createToolBar(ObservationTablePanel tablePanel) {
		var toolBar = new JToolBar();

		toolBar.add(createScanButton(tablePanel));
		toolBar.addSeparator();
		toolBar.add(new JLabel("Filter: "));
		toolBar.add(new JTextField(10));

		return toolBar;
	}
	
	private static JButton createScanButton(ObservationTablePanel tablePanel) {
		var button = new JButton("Scan");
		
		button.addActionListener(e -> {
			// Needs to be refactored...
			var scanner = new CompositeScanner();
			var store = new ObservationStore();
			scanner.scan(store);
						
			tablePanel.setStore(store);
		});
		
		return button;
	}

	private ToolBarFactory() { throw new AssertionError("Can not instanciate static class"); }
}
