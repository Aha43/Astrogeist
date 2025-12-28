package aha.common.ui.swing.tree;

import static java.util.Objects.requireNonNull;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public abstract class AbstractTreeModel implements TreeModel {
	private final EventListenerList listeners = new EventListenerList();
	
	private final Object root;
	
	protected AbstractTreeModel(Object root) {
		this.root = requireNonNull(root, "root"); }

	@Override public final Object getRoot() { return this.root; }

	@Override public void valueForPathChanged(TreePath path, Object newValue) {}

	@Override public final void addTreeModelListener(TreeModelListener l) {
		this.listeners.add(TreeModelListener.class, l); }

	@Override public final void removeTreeModelListener(TreeModelListener l) {
		this.listeners.remove(TreeModelListener.class, l); }
	
    protected final void fireStructureChanged(TreePath path) {
        var event = new TreeModelEvent(this, path);
        for (var l : listeners.getListeners(TreeModelListener.class))
            l.treeStructureChanged(event);
    }
    
    protected final void fireNodesChanged(TreePath path, int[] childIndices, 
    	Object[] children) {
    	
        var event = new TreeModelEvent(this, path, childIndices, children);
        for (var l : listeners.getListeners(TreeModelListener.class))
            l.treeNodesChanged(event);
    }

    protected final void fireNodesInserted(TreePath path, int[] childIndices,
    	Object[] children) {
        
    	var event = new TreeModelEvent(this, path, childIndices, children);
        for (var l : listeners.getListeners(TreeModelListener.class))
            l.treeNodesInserted(event);
    }

    protected final void fireNodesRemoved(TreePath path, int[] childIndices,
    	Object[] children) {
        
    	var event = new TreeModelEvent(this, path, childIndices, children);
        for (var l : listeners.getListeners(TreeModelListener.class))
            l.treeNodesRemoved(event);
    }
    
}
