package astrogeist.engine.observatory.edit;

import static aha.common.util.Strings.quote;
import static java.util.Objects.requireNonNull;

import astrogeist.engine.observatory.Instrument;

public final class ReplacedInstrument implements ConfigurationEditStep {
	private final Instrument instrument;
	private final Instrument replaced;
	
	public ReplacedInstrument(Instrument instrument, Instrument replaced) {
		this.instrument = requireNonNull(instrument, "instrument");
		this.replaced = requireNonNull(replaced, "replaced");
	}
	
	@Override public final EditAction action() { return EditAction.REPLACED; }
	@Override public final Instrument instrument() { return this.instrument; }

	@Override public final String toString() {
		return "replaced " + quote(this.replaced) + " with " +
			quote(this.instrument); 
	}
}
