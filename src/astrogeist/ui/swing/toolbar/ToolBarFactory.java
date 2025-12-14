package astrogeist.ui.swing.toolbar;

import javax.swing.JToolBar;

import aha.common.guard.ObjectGuards;
import astrogeist.ui.swing.App;

public final class ToolBarFactory {
	private ToolBarFactory() { ObjectGuards.throwStaticClassInstantiateError(); }
	
	public static JToolBar createToolBar(App app) {
		var toolBar = new JToolBar();

		toolBar.add(app.ScanAction);
		toolBar.addSeparator();

		return toolBar;
	}

}
