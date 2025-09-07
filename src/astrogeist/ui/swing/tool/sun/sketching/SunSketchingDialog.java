package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JSplitPane;

import astrogeist.engine.resources.Resources;

public final class SunSketchingDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public SunSketchingDialog() {
		setTitle("Sun");
		
		var folder = Resources.getUserDataDir();
		
		var sunPanel = new SunPanel();
		var sunControlsPanel = new SunControlsPanel(sunPanel, folder.toPath());
		var splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(sunPanel);
		splitPane.setRightComponent(sunControlsPanel);
		super.add(splitPane, BorderLayout.CENTER);
		var sunFeatureControlsPanel = new SunFeatureControlsPanel(sunPanel);
		super.add(sunFeatureControlsPanel, BorderLayout.NORTH);
		super.setSize(500, 600);
		super.pack();
	}

}
