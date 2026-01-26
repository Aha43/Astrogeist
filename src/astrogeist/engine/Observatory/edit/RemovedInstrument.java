package astrogeist.engine.observatory.edit;

import static aha.common.util.Strings.quote;
import static java.util.Objects.requireNonNull;

import astrogeist.engine.observatory.Instrument;

public final class RemovedInstrument implements ConfigurationEditStep {
	private final Instrument removed;
	
	public RemovedInstrument(Instrument removed) { 
		this.removed = requireNonNull(removed, "removed"); }
	
	@Override public final EditAction action() { return EditAction.REMOVED; }
	@Override public final Instrument instrument() { return removed; }
	
	@Override public final String toString() {
		return "removed " + quote(this.instrument().name()); }
}
