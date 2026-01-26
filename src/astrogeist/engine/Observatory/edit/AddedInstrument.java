package astrogeist.engine.observatory.edit;

import static aha.common.util.Strings.quote;
import static java.util.Objects.requireNonNull;

import astrogeist.engine.observatory.Instrument;

public final class AddedInstrument implements ConfigurationEditStep {
	private final Instrument added;
	
	public AddedInstrument(Instrument added) { 
		this.added = requireNonNull(added, "added"); }
	
	@Override public final EditAction action() { return EditAction.ADDED; }
	@Override public final Instrument instrument() { return added; }
	
	@Override public final String toString() {
		return "added " + quote(this.instrument().name()); }
}
