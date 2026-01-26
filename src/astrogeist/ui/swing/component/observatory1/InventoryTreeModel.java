package astrogeist.ui.swing.component.observatory1;

import static java.util.Objects.requireNonNull;

import aha.common.ui.swing.tree.AbstractTreeModel;
import astrogeist.engine.observatory1.InventoryNode;
import astrogeist.engine.observatory1.Observatory;

public final class InventoryTreeModel extends AbstractTreeModel {
	public InventoryTreeModel(Observatory observatory) {
		super(requireNonNull(observatory, "observatory").root()); }

	@Override public final Object getChild(Object parent, int idx) {
		return ((InventoryNode)parent).getChild(idx); }
	@Override public final int getChildCount(Object parent) {
		return ((InventoryNode)parent).getChildCount(); }
	@Override public boolean isLeaf(Object node) {
		return ((InventoryNode)node).isLeaf(); }
	@Override public final int getIndexOfChild(Object parent, Object child) {
		return ((InventoryNode)parent).getIndexOfChild((InventoryNode)child); }
}
