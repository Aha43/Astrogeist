package astrogeist.engine.observatory.edit;

import static aha.common.util.Strings.quote;
import static java.util.Objects.requireNonNull;

import astrogeist.engine.observatory.Item;

public final class ReplacedItem implements ConfigurationEditStep {
	private final Item item;
	private final Item replaced;
	
	public ReplacedItem(Item item, Item replaced) {
		this.item = requireNonNull(item, "item");
		this.replaced = requireNonNull(replaced, "replaced");
	}
	
	@Override public final EditAction action() { return EditAction.REPLACED; }
	@Override public final Item item() { return this.item; }

	@Override public final String toString() {
		return "replaced " + quote(this.replaced) + " with " +
			quote(this.item); 
	}
}
