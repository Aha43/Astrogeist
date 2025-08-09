package astrogeist.ui.swing.toolbar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import astrogeist.Common;
import astrogeist.engine.scanner.CompositeScanner;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.TimelineTablePanel;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class ToolBarFactory {
	public static JToolBar createToolBar(App app, TimelineTablePanel tablePanel) {
		var toolBar = new JToolBar();

		toolBar.add(createScanButton(app, tablePanel));
		toolBar.addSeparator();
		toolBar.add(new JLabel("Filter: "));
		toolBar.add(new JTextField(10));

		return toolBar;
	}
	
	private static JButton createScanButton(App app, TimelineTablePanel tablePanel) {
		var button = new JButton("Scan");
		
		button.addActionListener(e -> {
			try {
				var scanner = new CompositeScanner(app.getServices().getUserDataIo());
				
				var timeline = app.getServices().getTimeline();
				
				timeline.clear();
				scanner.scan(timeline);
				tablePanel.setData(timeline);
			} catch (Exception x) {
				MessageDialogs.showError("Failed to scan", x);
			}
			
		});
		
		return button;
	}

	private ToolBarFactory() { Common.throwStaticClassInstantiateError(); }
}
