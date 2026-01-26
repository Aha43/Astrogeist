package astrogeist.engine.observatory;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import aha.common.util.NamedList;
import astrogeist.engine.observatory.edit.AddedInstrument;
import astrogeist.engine.observatory.edit.ConfigurationEditStep;
import astrogeist.engine.observatory.edit.RemovedInstrument;
import astrogeist.engine.observatory.edit.ReplacedInstrument;

import static aha.common.guard.StringGuards.requireNonEmpty;

public final class Configuration {
	private final Observatory observatory;
	
	private final List<ConfigurationEditStep> edits = new ArrayList<>();
	
	private final NamedList<Instrument> instruments;
	
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
	
	public final String name() { return this.name; }
	
	public final Configuration base() { return this.base; }
	
	public final List<Instrument> instruments(){ 
		return this.instruments.values(); }
	
	public final List<String> instrumentNames() { 
		return this.instruments.names(); } 
	
	public final Configuration addInstrument(String name) {
		var instrument = this.observatory.getInstrument(name);
		this.instruments.add(instrument);
		this.edits.add(new AddedInstrument(instrument));
		return this;
	}
	
	public final Configuration addInstrumentBefore(String before, String name) {
		var instrument = this.observatory.getInstrument(name);
		this.instruments.addBeforeNamed(before, instrument);
		this.edits.add(new AddedInstrument(instrument));
		return this;
	}
	
	public final Configuration addInstrumentAfter(String after, String name) {
		var instrument = this.observatory.getInstrument(name);	
		this.instruments.addAfterNamed(after, instrument);
		this.edits.add(new AddedInstrument(instrument));
		return this;
	}
	
	public final Configuration removeInstrument(String name) {
		var removed = this.instruments.removeAndReturn(name);
		this.edits.add(new RemovedInstrument(removed));
		return this;
	}
	
	public final Configuration replaceInstrument(String old, String name) {
		var instrument = this.observatory.getInstrument(name);
		var replaced = this.instruments.replaceNamed(old, instrument);
		this.edits.add(new ReplacedInstrument(instrument, replaced));
		return this;
	}
	
	@Override public final String toString() {
		var sb = new StringBuilder();
		sb.append(this.name());
		var names = this.instruments.names();
		if (!names.isEmpty())
			sb.append(" : ").append(String.join(" - ", names));
		return sb.toString();
	}
	
}
