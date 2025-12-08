package aha.common.ui.swing.panels;

import static javax.swing.SwingUtilities.invokeLater;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * <p>
 *   Modal dialog for inspecting memory usage.
 * </p>
 */
public final class MemoryInspectorDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public MemoryInspectorDialog(Frame f) {
		super(f, "Memory Usage");
		super.setModal(false);
		
		super.add(new MemoryInspectorPanel(), BorderLayout.CENTER);
		
		var south = new JPanel(new FlowLayout(FlowLayout.CENTER));
		var close = new JButton("Close");
		close.addActionListener(e -> { dispose(); });
		south.add(close);
		super.add(south, BorderLayout.SOUTH);
		
		super.pack();
		
		super.setSize(200, 200);
	}
	
	public static void show(Frame f) {
		invokeLater(() -> { new MemoryInspectorDialog(f).setVisible(true); }); }
}
