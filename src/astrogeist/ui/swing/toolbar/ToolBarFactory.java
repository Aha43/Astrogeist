package astrogeist.ui.swing.toolbar;

import javax.swing.JButton;
import javax.swing.JToolBar;

import astrogeist.Common;
import astrogeist.engine.scanner.CompositeScanner;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.TimelineTablePanel;
import astrogeist.ui.swing.component.data.timeline.view.TimelineViewTablePanel;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class ToolBarFactory {
	public static JToolBar createToolBar(App app, TimelineTablePanel timelinePanel, TimelineViewTablePanel timelineViewTablePanel) {
		var toolBar = new JToolBar();

		toolBar.add(createScanButton(app, timelinePanel, timelineViewTablePanel));
		toolBar.addSeparator();

		return toolBar;
	}
	
	private static JButton createScanButton(App app, TimelineTablePanel timelineTabel, TimelineViewTablePanel timelineViewTablePanel) {
		var button = new JButton("Scan");
		
		button.addActionListener(e -> {
			try {
				var scanner = new CompositeScanner(app.getServices().getUserDataIo());
				
				var timeline = app.getServices().getTimeline();
				
				timeline.clear();
				scanner.scan(timeline);
				timelineTabel.setData(timeline);
				timelineViewTablePanel.setData(timeline);
			} catch (Exception x) {
				MessageDialogs.showError("Failed to scan", x);
			}
			
		});
		
		return button;
	}

	private ToolBarFactory() { Common.throwStaticClassInstantiateError(); }
}
