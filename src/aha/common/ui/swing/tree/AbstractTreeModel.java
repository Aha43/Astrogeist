package aha.common.ui.swing.tree;

import static java.util.Objects.requireNonNull;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * <p>
 *   Base class for
 *   {@link TreeModel} implementations-
 * </p>
 * <p>
 *   This is to be used as a base for pure custom implemented 
 *   {@link TreeModel}s, consider 
 *   {@link DefaultTreeModel} as part of deciding to utilize it.
 * </p>
 * @see DefaultTreeModel
 */
public abstract class AbstractTreeModel implements TreeModel {
	private final EventListenerList listeners = new EventListenerList();
	
	private final Object root;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param root the root of the tree.
	 */
	protected AbstractTreeModel(Object root) {
		this.root = requireNonNull(root, "root"); }

	@Override public final Object getRoot() { return this.root; }
	
	@Override public void valueForPathChanged(TreePath path, Object newValue) {}

	@Override public final void addTreeModelListener(TreeModelListener l) {
		this.listeners.add(TreeModelListener.class, l); }
	@Override public final void removeTreeModelListener(TreeModelListener l) {
		this.listeners.remove(TreeModelListener.class, l); }
	
	/**
	 * <p>
	 *   Fires
	 *   {@link TreeModelListener#treeStructureChanged(TreeModelEvent) 
	 *   tree structure changed events}.
	 * </p>
	 * @param path a {@link TreePath} object that identifies the path to the 
	 *             change.
	 */
    protected final void fireStructureChanged(TreePath path) {
        var event = new TreeModelEvent(this, path);
        for (var l : listeners.getListeners(TreeModelListener.class))
            l.treeStructureChanged(event);
    }
    
    /**
     * <p>
     *   Fires
     *   {@link TreeModelListener#treeNodesChanged(TreeModelEvent)
     *   tree nodes changed events}.
     * </p>
     * @param path a {@link TreePath} object that identifies the path to the 
	 *             change.
     * @param childIndices
     * @param children
     */
    protected final void fireNodesChanged(TreePath path, int[] childIndices, 
    	Object[] children) {
    	
        var event = new TreeModelEvent(this, path, childIndices, children);
        for (var l : listeners.getListeners(TreeModelListener.class))
            l.treeNodesChanged(event);
    }

    /**
     * <p>
     *   Fires
     *   {@link TreeModelListener#treeNodesInserted(TreeModelEvent) tree nodes 
     *   inserted events}
     * </p>
     * @param path         a {@link TreePath} object that identifies the path to
     *                     the change.
     * @param childIndices an array of int that specifies the index values of
     *                     the inserted items. The indices must be in sorted 
     *                     order, from lowest to highest.
     * @param children     an array of Object containing the inserted objects.
     */
    protected final void fireNodesInserted(TreePath path, int[] childIndices,
    	Object[] children) {
        
    	var event = new TreeModelEvent(this, path, childIndices, children);
        for (var l : listeners.getListeners(TreeModelListener.class))
            l.treeNodesInserted(event);
    }

    /**
     * <p>
     *   Fires
     *   {@link TreeModelListener#treeNodesRemoved(TreeModelEvent) tree nodes
     *   removed events}.
     * </p>
     * @param path         a {@link TreePath} object that identifies the path to
     *                     the change.
     * @param childIndices an array of int that specifies the index values of
     *                     the removed items. The indices must be in sorted 
     *                     order, from lowest to highest.
     * @param children     an array of Object containing the removed objects.
     */
    protected final void fireNodesRemoved(TreePath path, int[] childIndices,
    	Object[] children) {
        
    	var event = new TreeModelEvent(this, path, childIndices, children);
        for (var l : listeners.getListeners(TreeModelListener.class))
            l.treeNodesRemoved(event);
    }
    
}
