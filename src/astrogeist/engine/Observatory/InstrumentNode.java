package astrogeist.engine.Observatory;

import static aha.common.guard.Guards.requireNotSame;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import aha.common.exceptions.runtime.NotFoundException;
import aha.common.numbers.Unit;
import aha.common.numbers.UnitNumber;

public final class InstrumentNode {
	private final Instrument instrument;
	
	private final InstrumentNode parent;
	private final List<InstrumentNode> children = new ArrayList<>();
	
	public InstrumentNode(String name) {
		this.instrument = new Instrument(name);
		this.parent = null; 
	}
	
	public InstrumentNode(Instrument instrument) {
		this.instrument = new Instrument(instrument);
		this.parent = null;
	}
	
	public InstrumentNode(String name, InstrumentNode parent) {
		this.parent = requireNotSame(parent, this);
		this.instrument = new Instrument(name);
		parent.children.add(this);
	}
	
	public InstrumentNode(Instrument instrument, InstrumentNode parent) {
		this.parent = requireNotSame(parent, this);
		this.instrument = new Instrument(instrument);
		parent.children.add(this);
	}
	
	public final Instrument instrument() { return this.instrument; }
	
	public final InstrumentNode parent() { return this.parent; }
	public final boolean isRoot() { return this.parent == null; }
	public final boolean isLeaf() { return this.children.isEmpty(); }
	
	public final InstrumentNode from(String name) {
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
	public final InstrumentNode find(String name) {
		if (this.instrument.equals("name", name)) return this;
		
		for (var c : this.children) {
			var retVal = c.find(name);
			if (retVal != null) return retVal;
		}
		return null;
	}
	
	public final InstrumentNode with(String name) {
		return new InstrumentNode(name, this); }
	
	public final InstrumentNode with(Instrument instrument) {
		return new InstrumentNode(instrument, this); }
	
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
	public final InstrumentNode isSystem(String name) { 
		return this.with(name); } 
	
	public final InstrumentNode attribute(String name, Object value) {
		this.instrument.set(name, value);
		return this;
	}
	
	public final List<Instrument> system(String name) {
		var node = find(name);
		var stack = new Stack<InstrumentNode>();
		while (node != null) {
			stack.push(node);
			node = node.parent();
		}
		
		var retVal = new ArrayList<Instrument>();
		while (!stack.isEmpty()) retVal.add(stack.pop().instrument);
		return retVal;
	}
	
	// Convenient methods for setting common attributes
	
	public final InstrumentNode description(String desc) {
		return attribute("description", desc); }
	public final InstrumentNode serialNumber(String sn) {
		return attribute("serial-number", sn); }
	public final InstrumentNode apperature(int mm) {
		return attribute("apperature", new UnitNumber(mm, Unit.MM)); }
	public final InstrumentNode focalLength(int mm) {
		return attribute("focal-length", new UnitNumber(mm, Unit.MM)); }
	
	// Depth first search support 
	
	public final void dfs(InstrumentNodeVisitor visitor) { this.dfs(0, visitor); }
	
	private final void dfs(int level, InstrumentNodeVisitor visitor) {
		visitor.visit(level, this);
		level = level + 1;
		for (var child : this.children) child.dfs(level, visitor);
	}
	
	@Override public String toString() { return this.instrument.toString(); }
}
