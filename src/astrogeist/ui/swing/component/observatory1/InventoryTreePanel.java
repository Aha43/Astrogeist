package astrogeist.ui.swing.component.observatory1;

import static java.util.Objects.requireNonNull;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTree;

import aha.common.ui.swing.PanelWithNorthLabelAndSouthButtons;
import astrogeist.engine.observatory1.Observatory;

public final class InventoryTreePanel
	extends PanelWithNorthLabelAndSouthButtons {
	
	private static final long serialVersionUID = 1L;

	private final JTree tree = new JTree();
	
	public InventoryTreePanel() {
		super.contentInScroll(tree).label("Inventory Tree"); }
	
	public InventoryTreePanel(Observatory observatory) { 
		this(); data(observatory); } 
	
	public final void data(Observatory observatory) {
		this.tree.setModel(new InventoryTreeModel(
			requireNonNull(observatory, "observatory"))); }
	
	public static void showDialog(Frame frame, Observatory observatory) {
		var dlg = new JDialog(frame);
		dlg.add(new InventoryTreePanel(observatory), BorderLayout.CENTER);
		var south = new JPanel(new FlowLayout(FlowLayout.CENTER));
		var close = new JButton("Close");
		close.addActionListener(e -> dlg.setVisible(false));
		south.add(close);
		dlg.add(close, BorderLayout.SOUTH);
		dlg.pack();
		invokeLater(new Runnable() {
			public void run() { dlg.setVisible(true); } });
	}
}
