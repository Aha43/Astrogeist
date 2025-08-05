package astrogeist.ui.swing.toolbar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import astrogeist.Common;
import astrogeist.engine.scanner.CompositeScanner;
import astrogeist.engine.timeline.Timeline;
import astrogeist.ui.swing.component.data.timelineview.TimelineTablePanel;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class ToolBarFactory {
	public static JToolBar createToolBar(TimelineTablePanel tablePanel) {
		var toolBar = new JToolBar();

		toolBar.add(createScanButton(tablePanel));
		toolBar.addSeparator();
		toolBar.add(new JLabel("Filter: "));
		toolBar.add(new JTextField(10));

		return toolBar;
	}
	
	private static JButton createScanButton(TimelineTablePanel tablePanel) {
		var button = new JButton("Scan");
		
		button.addActionListener(e -> {
			try {
				var scanner = new CompositeScanner();
				var timeline = new Timeline();
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
