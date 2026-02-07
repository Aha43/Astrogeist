package astrogeist.engine.observatory;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static astrogeist.engine.observatory.Tag.normalize;
import static java.lang.String.join;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aha.common.util.NamedList;
import astrogeist.engine.observatory.edit.AddedInstrument;
import astrogeist.engine.observatory.edit.ConfigurationEditStep;
import astrogeist.engine.observatory.edit.RemovedInstrument;
import astrogeist.engine.observatory.edit.ReplacedInstrument;

public final class Configuration {
	private static final String SIG_VER = "cfgsig:v1";
	
	private final Observatory observatory;
	
	private final List<ConfigurationEditStep> edits = new ArrayList<>();
	
	private final NamedList<Instrument> instruments;
	
	private final Set<String> tags = new HashSet<>();
	
	private final Set<String> erasedTags = new HashSet<>();
	
	private final String name;
	
	private final Configuration base;
	
	Configuration(Observatory observatory, String name) {
		this.observatory = requireNonNull(observatory, "observatory"); 
		this.name = requireNonEmpty(name, "name").trim();
		this.instruments = new NamedList<>(Instrument::name);
		this.base = null;
	}
	
	Configuration(Configuration other, String name) {
		this.observatory = requireNonNull(other, "other").observatory;
		this.name = requireNonEmpty(name, "name").trim();
		this.instruments = new NamedList<>(other.instruments);
		this.base = other;
	}
	
	public final static String sigVer = "cfgsig:v1";
	
	private String id = null;
	
	public final String id() {
		if (id == null)
			id = SIG_VER + '|' + join("|", instruments.names());
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
		for (var curr : instruments.values()) tmp.addAll(this.findTags(curr));
		return Set.copyOf(tmp);
	}
	
	private final Set<String> findTags(Instrument instrument) {
		var retVal = new HashSet<>(this.tags);
		var itags = instrument.tags();
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
	
	public final String name() { return this.name; }
	
	public final Configuration base() { return this.base; }
	
	public final Observatory observatory() { return this.observatory; }
	
	public final List<Instrument> instruments() { 
		return this.instruments.values(); }
	
	public final List<String> instrumentNames() { 
		return this.instruments.names(); } 
	
	public final Configuration addInstrument(String name) {
		this.requireNotSealed();
		var instrument = this.observatory.getInstrument(name);
		this.instruments.add(instrument);
		this.edits.add(new AddedInstrument(instrument));
		return this;
	}
	
	public final Configuration addInstrumentBefore(String before, String name) {
		this.requireNotSealed();
		var instrument = this.observatory.getInstrument(name);
		this.instruments.addBeforeNamed(before, instrument);
		this.edits.add(new AddedInstrument(instrument));
		return this;
	}
	
	public final Configuration addInstrumentAfter(String after, String name) {
		this.requireNotSealed();
		var instrument = this.observatory.getInstrument(name);	
		this.instruments.addAfterNamed(after, instrument);
		this.edits.add(new AddedInstrument(instrument));
		return this;
	}
	
	public final Configuration removeInstrument(String name) {
		this.requireNotSealed();
		var removed = this.instruments.removeAndReturn(name);
		this.edits.add(new RemovedInstrument(removed));
		return this;
	}
	
	public final Configuration replaceInstrument(String old, String name) {
		this.requireNotSealed();
		var instrument = this.observatory.getInstrument(name);
		var replaced = this.instruments.replaceNamed(old, instrument);
		this.edits.add(new ReplacedInstrument(instrument, replaced));
		return this;
	}
	
	@Override public final String toString() {
		var sb = new StringBuilder();
		sb.append(this.name());
		var tags = this.tags();
		if (tags.size() > 0) 
			sb.append(" (").append(join(",", tags)).append(")");
		var names = this.instruments.names();
		if (!names.isEmpty())
			sb.append(" : ").append(join(" - ", names));
		return sb.toString();
	}
	
}
