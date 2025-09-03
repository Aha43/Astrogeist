package astrogeist.ui.swing.toolbar;

import javax.swing.JToolBar;

import astrogeist.Common;
import astrogeist.ui.swing.App;

public final class ToolBarFactory {
	private ToolBarFactory() { Common.throwStaticClassInstantiateError(); }
	
	public static JToolBar createToolBar(App app) {
		var toolBar = new JToolBar();

		toolBar.add(app.ScanAction);
		toolBar.addSeparator();

		return toolBar;
	}

}
