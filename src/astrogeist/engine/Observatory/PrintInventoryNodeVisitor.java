package astrogeist.engine.observatory;

import static aha.common.util.Strings.padding;

public class PrintInventoryNodeVisitor implements InventoryNodeVisitor {

	private final String ls = System.lineSeparator();
	
	private final StringBuilder sb = new StringBuilder();
	
	public final void clear() { this.sb.setLength(0); }
	
	@Override public final void visit(int level, InventoryNode node) {
		sb.
			append(padding(level)).
			append(node.instrument()).
			append(this.ls);
	}
	
	@Override public final String toString() { return sb.toString(); }
}
