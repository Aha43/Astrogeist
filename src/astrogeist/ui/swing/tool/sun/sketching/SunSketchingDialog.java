package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.BorderLayout;

import javax.swing.JDialog;

import astrogeist.engine.resources.Resources;

public final class SunSketchingDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public SunSketchingDialog() {
		setTitle("Sun");
		
		var folder = Resources.getUserDataDir();
		
		var sunPanel = new SunPanel();
		var sunControlsPanel = new SunControlsPanel(sunPanel, folder.toPath());
		super.add(sunPanel, BorderLayout.CENTER);
		super.add(sunControlsPanel, BorderLayout.SOUTH);
		super.setSize(500, 600);
		super.pack();
	}

}
