package astrogeist.engine.observatory;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static astrogeist.engine.observatory.Tag.normalize;
import static java.lang.String.join;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aha.common.abstraction.IndexedAndNamed;
import aha.common.util.NamedList;
import astrogeist.engine.observatory.edit.AddedItem;
import astrogeist.engine.observatory.edit.ConfigurationEditStep;
import astrogeist.engine.observatory.edit.RemovedItem;
import astrogeist.engine.observatory.edit.ReplacedItem;

/**
 * <p>
 *   A configuration of
 *   {@link Item}s.
 * </p>
 */
public final class Configuration implements IndexedAndNamed {
	private static final String SIG_VER = "cfgsig:v1";
	
	private final Axis axis;
	
	private final List<ConfigurationEditStep> edits = new ArrayList<>();
	
	private final NamedList<Item> items;
	
	private final Set<String> tags = new HashSet<>();
	
	private final Set<String> erasedTags = new HashSet<>();
	
	private String name;
	
	private final Configuration base;
	
	Configuration(Axis axis, String name) {
		this.axis = requireNonNull(axis, "axis"); 
		this.name = requireNonEmpty(name, "name").trim();
		this.items = new NamedList<>(Item::name);
		this.base = null;
	}
	
	Configuration(Configuration other, String name) {
		this.axis = requireNonNull(other, "other").axis;
		this.name = requireNonEmpty(name, "name").trim();
		this.items = new NamedList<>(other.items);
		this.base = other;
	}
	
	public final static String sigVer = "cfgsig:v1";
	
	private String id = null;
	
	/**
	 * <p>
	 *   Gets the stable id.
	 * </p>
	 * <p>
	 *   This seals this
	 *   {@link Configuration}, meaning items now are fixed since id is
	 *   constructed from items.
	 * </p>
	 */
	public final String id() {
		if (id == null)
			id = SIG_VER + '|' + join("|", items.names());
	    return id;
	}
	
	private void requireNotSealed() {
	    if (id == null) return;
	    throw new IllegalStateException(
	    	"Configuration is sealed (id already computed): name=" + name +
	    	", id=" + id);
	}
	
	public Configuration tag(String tag) {
		this.tags.add(normalize(tag));
		return this;
	}
	
	public final Set<String> tags() {
		var tmp = new HashSet<String>();
		for (var curr : items.values()) tmp.addAll(this.findTags(curr));
		return Set.copyOf(tmp);
	}
	
	private final Set<String> findTags(Item item) {
		var retVal = new HashSet<>(this.tags);
		var itags = item.tags();
		for (var curr : itags) {
			if (this.erasedTags.contains(curr)) continue;
			retVal.add(curr);
		}
		return retVal;
	}

	public final boolean hasTag(String tag) {
		return tags().contains(normalize(tag)); }
	
	public final Configuration eraseTag(String tag) {
		this.erasedTags.add(normalize(tag));
		return this;
	}
	
	/**
	 * <p>
	 *   Gets the name of configuration that.
	 * </p>
	 * <p>
	 *   While the
	 *   {@link #id() id} is stable this can change and is for end user eyes.
	 * </p>
	 * @return the name.
	 */
	public final String name() { return this.name; }
	
	/**
	 * <p>
	 *   Sets the name.
	 * </p>
	 * @param name the new name.
	 * @throws NullPointerException if {@code name == null}.
	 * @throws IllegalArgumentException if {@code name} is the empty string.
	 */
	public final void name(String name) {
		this.name = requireNonEmpty(name, "name"); }
	
	public final Configuration base() { return this.base; }
	
	public final Axis axis() { return this.axis; }
	
	public final List<Item> items() { return this.items.values(); }
	
	public final List<String> itemNames() { return this.items.names(); } 
	
	public final Configuration addItem(String name) {
		this.requireNotSealed();
		var item = this.axis.getItem(name);
		this.items.add(item);
		this.edits.add(new AddedItem(item));
		return this;
	}
	
	public final Configuration addItemBefore(String before, String name) {
		this.requireNotSealed();
		var item = this.axis.getItem(name);
		this.items.addBeforeNamed(before, item);
		this.edits.add(new AddedItem(item));
		return this;
	}
	
	public final Configuration addItemAfter(String after, String name) {
		this.requireNotSealed();
		var item = this.axis.getItem(name);	
		this.items.addAfterNamed(after, item);
		this.edits.add(new AddedItem(item));
		return this;
	}
	
	public final Configuration removeItem(String name) {
		this.requireNotSealed();
		var removed = this.items.removeAndReturn(name);
		this.edits.add(new RemovedItem(removed));
		return this;
	}
	
	public final Configuration replaceItem(String old, String name) {
		this.requireNotSealed();
		var item = this.axis.getItem(name);
		var replaced = this.items.replaceNamed(old, item);
		this.edits.add(new ReplacedItem(item, replaced));
		return this;
	}
	
	@Override public final String toString() {
		var sb = new StringBuilder();
		sb.append(this.name());
		var tags = this.tags();
		if (tags.size() > 0) 
			sb.append(" (").append(join(",", tags)).append(")");
		var names = this.items.names();
		if (!names.isEmpty())
			sb.append(" : ").append(join(" - ", names));
		return sb.toString();
	}
	
}
