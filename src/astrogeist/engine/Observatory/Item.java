package astrogeist.engine.observatory;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.guard.StringGuards.requireNotHaveAny;
import static astrogeist.engine.observatory.Tag.normalize;
import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;

import aha.common.util.AttributeBase;
import aha.common.util.Strings;

public final class Item extends AttributeBase<Item> {
	private final String name;
	
	private final Set<String> tags = new HashSet<>();
	
	public Item(String name) {
		name = requireNonEmpty(name, "name").trim();
		name = requireNotHaveAny("| ", name, "name");
		this.name = name;
	}
	
	public Item(Item o) { 
		super(requireNonNull(o, "o"));
		this.name = o.name();
		this.tags.addAll(o.tags);
	}
	
	public final Item tag(String tag) {
		this.tags.add(normalize(tag));
		return this;
	}
	
	public final boolean hasTag(String tag) { 
		return this.tags.contains(normalize(tag)); }

	public final Set<String> tags(){ return Set.copyOf(this.tags); }
	
	public final String name() { return this.name; }
	
	public final Item description(String description) {
		return super.set("description", 
			requireNonEmpty(description, "description")); }
	
	public final String description() {
		return super.getAsString("description", Strings.EMPTY); }
}
