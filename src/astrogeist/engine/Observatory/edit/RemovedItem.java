package astrogeist.engine.observatory.edit;

import static aha.common.util.Strings.quote;
import static java.util.Objects.requireNonNull;

import astrogeist.engine.observatory.Item;

public final class RemovedItem implements ConfigurationEditStep {
	private final Item removed;
	
	public RemovedItem(Item removed) { 
		this.removed = requireNonNull(removed, "removed"); }
	
	@Override public final EditAction action() { return EditAction.REMOVED; }
	@Override public final Item item() { return removed; }
	
	@Override public final String toString() {
		return "removed " + quote(this.item().name()); }
}
