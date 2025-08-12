package astrogeist.ui.swing.toolbar;

import javax.swing.JButton;
import javax.swing.JToolBar;

import astrogeist.Common;
import astrogeist.engine.scanner.CompositeScanner;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class ToolBarFactory {
	private ToolBarFactory() { Common.throwStaticClassInstantiateError(); }
	
	public static JToolBar createToolBar(App app) {
		var toolBar = new JToolBar();

		toolBar.add(createScanButton(app));
		toolBar.addSeparator();

		return toolBar;
	}
	
	private static JButton createScanButton(App app) {
		var button = new JButton("Scan");
		
		button.addActionListener(e -> {
			try {
				var scanner = new CompositeScanner(app.getServices().getUserDataIo());
				
				var timeline = app.getServices().getTimeline();
				
				timeline.clear();
				scanner.scan(timeline);
				app.getSearchPanel().setTimelineView(timeline);
				app.getTimelinePanel().setTimeline(timeline);
			} catch (Exception x) {
				MessageDialogs.showError("Failed to scan", x);
			}
			
		});
		
		return button;
	}

}
