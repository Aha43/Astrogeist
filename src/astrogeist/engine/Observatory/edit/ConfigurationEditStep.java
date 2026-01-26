package astrogeist.engine.observatory.edit;

import astrogeist.engine.observatory.Instrument;

public interface ConfigurationEditStep {
	EditAction action();
	Instrument instrument();
}
