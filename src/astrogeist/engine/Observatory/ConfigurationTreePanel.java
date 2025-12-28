package astrogeist.engine.observatory;

import static java.util.Objects.requireNonNull;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;

public final class ConfigurationTreePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private ConfigurationsTreeModel model = null;
	private final JTree tree = new JTree();
	
	public ConfigurationTreePanel() {
		super(new BorderLayout());
		this.tree.setRootVisible(false);
		this.tree.setCellRenderer(ConfigurationTreeCellRenderer.INSTANCE);
		super.add(new JScrollPane(this.tree), BorderLayout.CENTER); 
	}
	
	public final void data(Observatory observatory) {
		this.model = new ConfigurationsTreeModel(
			requireNonNull(observatory, "observatory"));
		this.tree.setModel(this.model);
	}
	
	public final void addSelectionListener(TreeSelectionListener l) {
		this.tree.addTreeSelectionListener(l); }
	
	public final void removeSelectionListener(TreeSelectionListener l) {
		this.tree.removeTreeSelectionListener(l); }

	public final static void showDialog(Observatory observatory) {
		var dlg = new JDialog();
		var panel = new ConfigurationTreePanel();
		panel.data(observatory);
		dlg.add(panel);
		dlg.pack();
		dlg.setSize(500, 500);
		invokeLater(new Runnable() {
			@Override public void run() { dlg.setVisible(true); } });
	}
}
