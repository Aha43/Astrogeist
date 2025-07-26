package astrogeist.app.toolbar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public final class ToolBarFactory {
	
	public static JToolBar createToolBar() {
		var toolBar = new JToolBar();

		toolBar.add(new JButton("Rescan"));
		toolBar.add(new JButton("Export"));
		toolBar.addSeparator();
		toolBar.add(new JLabel("Filter: "));
		toolBar.add(new JTextField(10));

		return toolBar;
	}

	private ToolBarFactory() { throw new AssertionError("Can not instanciate static class"); }
}
