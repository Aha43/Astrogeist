package astrogeist.engine.observatory.edit;

import static aha.common.util.Strings.quote;
import static java.util.Objects.requireNonNull;

import astrogeist.engine.observatory.Item;

public final class AddedItem implements ConfigurationEditStep {
	private final Item added;
	
	public AddedItem(Item added) { 
		this.added = requireNonNull(added, "added"); }
	
	@Override public final EditAction action() { return EditAction.ADDED; }
	@Override public final Item item() { return added; }
	
	@Override public final String toString() {
		return "added " + quote(this.item().name()); }
}
