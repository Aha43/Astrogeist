package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import astrogeist.engine.resources.Resources;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.DialogBase;

public final class SunSketchingDialog extends DialogBase {
	private static final long serialVersionUID = 1L;
	
	public SunSketchingDialog(App app) {
		super(app, "Sun", false, true);
		
		var folder = Resources.getUserDataDir();
		
		var contentPanel = new JPanel(new BorderLayout());
		
		var sunPanel = new SunPanel();
		var sunControlsPanel = new SunControlsPanel(sunPanel, folder.toPath());
		var splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(sunPanel);
		splitPane.setRightComponent(sunControlsPanel);
		contentPanel.add(splitPane, BorderLayout.CENTER);
		var sunFeatureControlsPanel = new SunFeatureControlsPanel(sunPanel);
		contentPanel.add(sunFeatureControlsPanel, BorderLayout.NORTH);
		super.setContent(contentPanel);
		super.pack();
		super.setSize(500, 500);
	}

}
