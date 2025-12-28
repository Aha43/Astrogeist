package astrogeist.engine.observatory;

import static aha.common.guard.ObjectGuards.requireNotSame;
import static aha.common.guard.StringGuards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import aha.common.exceptions.runtime.NotFoundException;
import aha.common.util.Strings;

public final class InventoryNode {
	private final String name;
	private String description = Strings.EMPTY;
	private Instrument instrument;
	
	private final InventoryNode parent;
	private final List<InventoryNode> children = new ArrayList<>();
	
	private InventoryNode(String name) {
		this.name = requireNonEmpty(name, "name");
		this.instrument = null;
		this.parent = null; 
	}
	
	private InventoryNode(String name, InventoryNode parent) {
		this.parent = requireNotSame(parent, this);
		this.name = requireNonEmpty(name, "name");
		this.instrument = null;
		parent.children.add(this);
	}
	
	public InventoryNode(Instrument instrument, InventoryNode parent) {
		this.parent = requireNotSame(parent, this);
		this.name = requireNonNull(instrument, "instrument").name();
		this.instrument = new Instrument(instrument);
		parent.children.add(this);
	}
	
	public final String name() { return this.name; }
	
	public final Instrument instrument() { return this.instrument; }
	
	public final InventoryNode parent() { return this.parent; }
	public final boolean isRoot() { return this.parent == null; }
	public final boolean isLeaf() { return this.children.isEmpty(); }
	
	public final InventoryNode from(String name) {
		var retVal = this.find(name);
		if (retVal != null) return retVal;
		throw new NotFoundException(name);
	}
	
	/**
	 * <p>
	 *   Finds named node.
	 * </p>
	 * @param name the name of the searched node.
	 * @return the node or {@code null} if not found.
	 */
	public final InventoryNode find(String name) {
		if (this.name.equals(name)) return this;
		
		for (var c : this.children) {
			var retVal = c.find(name);
			if (retVal != null) return retVal;
		}
		return null;
	}
	
	public final InventoryNode with(String name) {
		return new InventoryNode(name, this); }
	
	public final InventoryNode with(Instrument instrument) {
		return new InventoryNode(instrument, this); }
	
	/**
	 * <p>
	 *   Makes a node which only purpose is to say that the path from here to
	 *   root is a 'system'.
	 * </p>
	 * <p>
	 *   Is a pure renaming of 
	 *   {@link #with(String)}.
	 * </p>
	 * @param name the name of system being defined.
	 * @return the created node.
	 */
	public final InventoryNode isSystem(String name) { 
		return this.with(name); } 
	
	public final InventoryNode having(Object value) {
		if (this.instrument == null) 
			this.instrument = new Instrument(this.name);
		this.instrument.set(value);
		return this;
	}
	
	public final InventoryNode having(String name, Object value) {
		if (this.instrument == null) 
			this.instrument = new Instrument(this.name);
		this.instrument.set(name, value);
		return this;
	}
	
	public final ObservatorySystem system(String name) {
		return this.system(name, null); }
	
	public final ObservatorySystem system(String name, String displayName) {
		var node = find(name);
		if (node == null) throw new NotFoundException(name);
		
		var stack = new Stack<InventoryNode>();
		while (node != null) {
			stack.push(node);
			node = node.parent();
		}
		
		var retVal = displayName == null ? new ObservatorySystem(name) :
			new ObservatorySystem(name, displayName);
		
		while (!stack.isEmpty()) {
			var instrument = stack.pop().instrument();
			if (instrument != null) retVal.add(instrument);
		}
		return retVal;
	}
	
	// Convenient methods for setting common attributes
	
	public final InventoryNode description(String desc) {
		this.description = requireNonEmpty(desc, "desc");
		return this;
	}
	
	public final String description() { return this.description; }
	
	public final InventoryNode serialNumber(String sn) {
		return having("serial-number", sn); }
	
	// Depth first search support 
	
	public final void dfs(InventoryNodeVisitor visitor) { 
		this.dfs(0, visitor); }
	
	private final void dfs(int level, InventoryNodeVisitor visitor) {
		visitor.visit(level, this);
		level = level + 1;
		for (var child : this.children) child.dfs(level, visitor);
	}
	
	@Override public String toString() { return this.instrument.toString(); }
	
	public final static InventoryNode createRoot(String name) { 
		return new InventoryNode(name); }
	
}
