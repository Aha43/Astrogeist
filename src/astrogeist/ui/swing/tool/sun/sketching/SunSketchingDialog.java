package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.BorderLayout;

import javax.swing.JDialog;

public final class SunSketchingDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public SunSketchingDialog() {
		var sunPanel = new SunPanel();
		var sunControlsPanel = new SunControlsPanel(sunPanel);
		super.add(sunPanel, BorderLayout.CENTER);
		super.add(sunControlsPanel, BorderLayout.SOUTH);
		super.setSize(500, 600);
		super.pack();
	}

}
