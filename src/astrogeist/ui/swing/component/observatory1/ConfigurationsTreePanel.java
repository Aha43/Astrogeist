package astrogeist.ui.swing.component.observatory1;

import static aha.common.ui.swing.AhaSwingUtil.treePath;
import static aha.common.util.Cast.as;
import static aha.common.util.Strings.isNullOrBlank;
import static java.util.Objects.requireNonNull;
import static javax.swing.SwingUtilities.invokeLater;

import javax.swing.JDialog;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import aha.common.ui.swing.PanelWithNorthLabelAndSouthButtons;
import astrogeist.engine.observatory1.Configuration;
import astrogeist.engine.observatory1.Observatory;

public final class ConfigurationsTreePanel
	extends PanelWithNorthLabelAndSouthButtons
{
	private static final long serialVersionUID = 1L;
	
	private ConfigurationsTreeModel model = null;
	private final JTree tree = new JTree();
	
	public ConfigurationsTreePanel() {
		this.tree.setRootVisible(false);
		this.tree.setCellRenderer(ConfigurationTreeCellRenderer.INSTANCE);
		this.tree.getSelectionModel().setSelectionMode(
			TreeSelectionModel.SINGLE_TREE_SELECTION);
		super.contentInScroll(tree).label("Configurations"); 
	}
	
	public final void data(Observatory observatory, String selected) {
		this.model = new ConfigurationsTreeModel(
			requireNonNull(observatory, "observatory"));
		this.tree.setModel(this.model);
		if (!isNullOrBlank(selected)) {
			var configuration = this.model.getByCode(selected);
			var path = treePath(this.model.getRoot(), configuration);
			this.tree.setSelectionPath(path);
		}
	}
	
	public final void addTreeSelectionListener(TreeSelectionListener l) {
		this.tree.addTreeSelectionListener(l); }
	
	public final void removeTreeSelectionListener(TreeSelectionListener l) {
		this.tree.removeTreeSelectionListener(l); }
	
	public final Configuration selected() {
		var path = this.tree.getSelectionModel().getSelectionPath();
		if (path == null || path.getPathCount() < 1) return null;
		for (var c : path.getPath()) {
			var retVal = as(Configuration.class, c);
			if (retVal != null) return retVal;
		}
		return null;
	}

	public final static void showDialog(Observatory observatory) {
		var dlg = new JDialog();
		var panel = new ConfigurationsTreePanel();
		panel.data(observatory, null);
		dlg.add(panel);
		dlg.pack();
		dlg.setSize(500, 500);
		invokeLater(new Runnable() {
			@Override public void run() { dlg.setVisible(true); } });
	}
}
