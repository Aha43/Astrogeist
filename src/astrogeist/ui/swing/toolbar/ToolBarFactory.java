package astrogeist.ui.swing.toolbar;

import javax.swing.JToolBar;

import aha.common.util.Guards;
import astrogeist.ui.swing.App;

public final class ToolBarFactory {
	private ToolBarFactory() { Guards.throwStaticClassInstantiateError(); }
	
	public static JToolBar createToolBar(App app) {
		var toolBar = new JToolBar();

		toolBar.add(app.ScanAction);
		toolBar.addSeparator();

		return toolBar;
	}

}
